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

package onextent.bluecql

import org.apache.cassandra.cql3.statements.CreateTableStatement

object DbCode {

  def apply(keyspace: String, pkg: String, statements: Iterator[CreateTableStatement.RawStatement]): String = {

    var objCode = ""

    for (stmt <- statements) {
      objCode = objCode +
s"""
  object ${stmt.columnFamily()} extends ${stmt.columnFamily()} with keyspace.Connector
""".stripMargin
    }

    val code =
s"""package $pkg

import scala.concurrent.Future
import com.websudos.phantom.dsl._

object Defaults {
  val keyspace = sys.env.get("KEYSPACE").getOrElse("$keyspace")
  val host = sys.env.get("CASSANDRA_HOST").getOrElse("localhost") //todo: get seq
  val hosts = Seq(host)
  val connector = ContactPoints(hosts, 9042).keySpace(keyspace)
}

class Db

class Db(val keyspace: KeySpaceDef) extends Database(keyspace) {
  $objCode
}

object Db extends Db(Defaults.connector)

""".stripMargin
    code
  }
}

