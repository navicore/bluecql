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

import java.io.{File, PrintWriter}
import java.net.{InetSocketAddress, SocketAddress}

import onextent.bluecql.cql.Statements
import org.apache.cassandra.cql3.statements.CreateTableStatement

object TableCode extends CodeGenerator {

  def apply(): Unit = {
    val statements: Iterator[CreateTableStatement.RawStatement] = Statements.tables()
    for (stmt <- statements) {
      val sloc = pdir() + "/store"
      val sdir = new File(sloc).mkdirs()
      val file = s"${sloc}/Db${stmt.columnFamily()}.scala"
      val code = applyStmt(property(PACKAGE_PROP), stmt)
      new PrintWriter(file) { write(s"$code\n"); close }
    }
  }

  def fields(stmt: CreateTableStatement.RawStatement): String = {
    //org.apache.cassandra.config.Config.setClientMode(true)
    //stmt.prepareKeyspace(ClientState.forExternalCalls(new InetSocketAddress("localhost", 9042)))
    //stmt.prepareKeyspace("iot")
    //val pstmt = stmt.prepare().statement.asInstanceOf[CreateTableStatement]
    //stmt.prepare()
    val tname = stmt.columnFamily()
    val cname = caseName(tname)
    //println(pstmt.getCFMetaData().allColumns())
    s"""
       | object id extends StringColumn(this) with PartitionKey[String]
       | object ids extends StringColumn(this) with PartitionKey[String]
     """.stripMargin
  }

  def unmarshal(stmt: CreateTableStatement.RawStatement): String = {
    val tname = stmt.columnFamily()
    val cname = caseName(tname)
    s"""
       |    ${cname}(id(row))
       |    ${cname}(ids(row))
     """.stripMargin
  }

  def applyStmt(pkg: String, stmt: CreateTableStatement.RawStatement): String = {
    val tname = stmt.columnFamily()
    val cname = caseName(tname)

    val code =s"""package ${pkg}.store

import scala.concurrent.Future
import com.websudos.phantom.dsl._
import ${property(PACKAGE_PROP)}._

class Db${stmt.columnFamily()} extends CassandraTable[${stmt.columnFamily()}, ${cname}] {

  ${fields(stmt)}

  def fromRow(row: Row): ${cname} = {
    ${unmarshal(stmt)}
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

