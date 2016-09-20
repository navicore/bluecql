// Copyright (C) 2016-2017 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package onextent.bluecql.gen

import java.io.PrintWriter

import onextent.bluecql.Config
import org.apache.cassandra.cql3.statements.CreateTableStatement

object RouterCode extends CodeGenerator with Config {

  def apply(keyspace: String, statements: Iterator[CreateTableStatement.RawStatement]): Unit = {
    var routes = ""
    for (stmt <- statements) {
      val tname = stmt.columnFamily()
      val cname = caseName(tname)
      routes = routes +
s"""path("${tname}" / Segment) { (id) =>
            complete("ok")
          } ~
          path("${tname}") {
            complete("ok")
          } ~
""".stripMargin
      routes = routes.substring(0, routes.length() - 1) // chomp lf ~
    }
    routes = routes.substring(0, routes.length() - 1) // chomp last ~
    val code =
      s"""package ${property(PACKAGE_PROP)}

import akka.actor.Actor
import spray.http.MediaTypes
import spray.routing._

class ServiceActor() extends Actor with Route {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait Route extends HttpService with DbDirectives {
  val route = {
    pathPrefix("$keyspace") {
      respondWithMediaType(MediaTypes.`application/json`) {
        get {
          ${routes}
        }
      }
    }
  }
}

""".stripMargin

    val file = s"${pdir}/ServiceActor.scala"
    new PrintWriter(file) { write(s"$code"); close }

  }
}
