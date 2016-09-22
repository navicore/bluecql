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

package onextent.bluecql.cql

import onextent.bluecql.Config
import onextent.bluecql.antlr4.CQL3Parser.StatementsContext
import onextent.bluecql.antlr4.{CQL3Lexer, CQL3Parser}
import org.antlr.v4.runtime.{ANTLRFileStream, CommonTokenStream}
import org.apache.cassandra.cql3.statements.{CreateKeyspaceStatement, CreateTableStatement, CreateTypeStatement, ParsedStatement}

object PStatements extends Config {
  private val fileStream = new ANTLRFileStream(property(FILE_PROP), "utf8")
  private val lexer = new CQL3Lexer(fileStream)
  private val token = new CommonTokenStream(lexer)
  private val parser = new CQL3Parser(token)
  val statements: StatementsContext = parser.statements()
}

