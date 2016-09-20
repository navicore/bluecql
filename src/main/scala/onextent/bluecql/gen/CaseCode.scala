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

import onextent.bluecql.cql.Statements

object CaseCode extends CodeGenerator {

  def apply(): Unit = {
    var cases = ""
    for (stmt <- Statements.tables()) {
      val cname = caseName(stmt.columnFamily())
      cases = cases +
s"""case class ${cname}(id:String) extends DbData
""".stripMargin + "\n"
    }

    var jproto = ""
    for (stmt <- Statements.tables()) {
      val cname = caseName(stmt.columnFamily())
      jproto = jproto +
s"""object ${cname}JsonProtocol extends DefaultJsonProtocol {
    implicit val ${fieldName(cname)} = jsonFormat${argCnt(stmt)}($cname)
}""".stripMargin + "\n"
    }

    val code =
      s"""package ${property(PACKAGE_PROP)}

import spray.json._

sealed abstract class DbData

${cases}
${jproto}
""".stripMargin

    val file = s"${pdir}/DbData.scala"
    new PrintWriter(file) { write(s"$code"); close }

  }
}
