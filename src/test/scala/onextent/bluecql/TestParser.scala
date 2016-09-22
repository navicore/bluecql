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

import onextent.bluecql.antlr4.CQL3Parser.{Create_keyspace_stmtContext, Create_table_stmtContext, Create_type_stmtContext}
import onextent.bluecql.cql.PStatements
import org.scalatest.FlatSpec

class TestParser extends FlatSpec with Config {

  val testcql = getClass.getResource("/test.cql").getPath
  property(FILE_PROP, testcql)
  property(PACKAGE_PROP, "onextent.bluecql.test")
  private def fixture =
    new {
      val ctx = PStatements.statements
    }

  it should "get each parsed stmt" in {
    val f = fixture
    println("ejs sz: " + f.ctx.statement().size())
    for (i <- 0 to f.ctx.statement().size() - 1) {
      println("cl: " + f.ctx.statement(i).getChild(0).getClass.getName)
      f.ctx.statement(i).getChild(0) match {
        case o: Create_type_stmtContext => {
          println("ejs got type name " + o.type_name().getText())
          for (i <- 0 to o.column_definitions().column_definition().size() - 1) {
            println("  ejs type ****** " + o.column_definitions().column_definition(i).getClass.getName)
            println("  ejs cn " + o.column_definitions().column_definition(i).column_name.getText)
            println("  ejs cn " + o.column_definitions().column_definition(i).column_type().getText)
          }

        }
        case o: Create_keyspace_stmtContext => {
          println("ejs got create keyspace")
        }
        case o: Create_table_stmtContext => {
          println("ejs got table name " + o.table_name().getText())
          for (i <- 0 to o.column_definitions().column_definition().size() - 1) {
            println("  ejs table ******************* " + o.column_definitions().column_definition(i).getClass.getName)
            println("  ejs cn " + o.column_definitions().column_definition(i).column_name.getText)
            println("  ejs cn " + o.column_definitions().column_definition(i).column_type().getText)
          }
        }
        case _ => println("ejs got others")
      }
    }
  }
}

