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

import org.apache.cassandra.cql3.statements.CreateTableStatement

object DbDirectives extends CodeGenerator {

  def apply(keyspace: String, pkg: String, statements: Iterator[CreateTableStatement.RawStatement], pdir: String): Unit = {
    val code =
      s"""package $pkg

import spray.routing._
import spray.json._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.{Failure, Success}

trait DbDirectives extends HttpService {

}

""".stripMargin

    val file = s"${pdir}/DbDirectives.scala"
    new PrintWriter(file) { write(s"$code"); close }

  }
}
