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

object MainCode extends Config {

  def apply(): Unit = {
    val code =
s"""package ${property(PACKAGE_PROP)}
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory
import spray.can.Http
import org.apache.log4j.BasicConfigurator
import org.slf4j.LoggerFactory

object Main extends App {

  BasicConfigurator.configure();
  val logger = LoggerFactory.getLogger(Main.getClass.getName)

  object Args extends ScallopConf(args) {
    // http
    val svcname = opt[String]("svcname", descr = "ServiceActor name", default = Some("bluecql"))
    val basepath = opt[String]("basepath", descr = "url path prefix", default = Some("store"))
    val host = opt[String]("host", descr = "ifc to listen on", default = Some("0.0.0.0"))
    val port = opt[Int]("port", descr = "port to listen on", default = Some(8081))
  }
  Args.verify()

  sys.props.put("basepath", Args.basepath())

  implicit val system = ActorSystem(Args.svcname())
  val service = system.actorOf(Props(new ServiceActor()), name = Args.svcname())

  IO(Http) ! Http.Bind(service, Args.host(), port = Args.port())
}

""".stripMargin

    val file = s"${pdir}/Main.scala"
    new PrintWriter(file) { write(s"$code"); close }

  }
}

