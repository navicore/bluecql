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

object TableCode extends CodeGenerator {

  def apply(statements: Iterator[CreateTableStatement.RawStatement], pkg: String, pdir: String): Unit = {
    for (stmt <- statements) {
      val file = s"${pdir}/Db${stmt.columnFamily()}.scala"
      val code = applyStmt(pkg, stmt)
      new PrintWriter(file) { write(s"$code\n"); close }
    }
  }
  def applyStmt(pkg: String, stmt: CreateTableStatement.RawStatement): String = {
    val tname = stmt.columnFamily()
    val cname = caseName(tname)
    val code =s"""package $pkg

import scala.concurrent.Future
import com.websudos.phantom.dsl._

class Db${stmt.columnFamily()} extends CassandraTable[${stmt.columnFamily()}, ${cname}] {

  object id extends StringColumn(this) with PartitionKey[String]
  //todo: iterate over stmt for values

  def fromRow(row: Row): ${cname} = {
    ${cname}(id(row))
    //todo: iterate over stmt for values
  }
}

abstract class ${tname} extends Db${stmt.columnFamily()} with RootConnector {
  def getById(id: String): Future[Option[${cname}]] = {
    select.where(_.id eqs id).one()
  }
  def getAll(): Future[List[${cname}]] = {
    select.fetch()
  }
}

""".stripMargin
    code
  }
}

