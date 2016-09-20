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
import org.antlr.runtime.{ANTLRFileStream, CommonTokenStream}
import org.apache.cassandra.cql3.statements.{CreateKeyspaceStatement, CreateTableStatement, CreateTypeStatement, ParsedStatement}
import org.apache.cassandra.cql3.{CqlLexer, CqlParser}

class Statements() extends Iterator[ParsedStatement] with Config {
  private val fileStream = new ANTLRFileStream(property(FILE_PROP), "utf8")
  private val lexer = new CqlLexer(fileStream)
  private val token = new CommonTokenStream(lexer)
  private val parser = new CqlParser(token)
  private var nextStmt: ParsedStatement = parser.query()
  def hasNext: Boolean = nextStmt != null
  def next(): ParsedStatement = {
    val current = nextStmt
    nextStmt = parser.query()
    current
  }
}

object Statements {
  def apply(classname: String): Iterator[ParsedStatement] = {
    for {
      stmt <- new Statements()
      if stmt.getClass().getName.matches(s".*$classname")
    }  yield stmt
  }
  def keyspaces(): Iterator[CreateKeyspaceStatement] = {
    apply("CreateKeyspaceStatement").map(t => t.asInstanceOf[CreateKeyspaceStatement])
  }
  def types(): Iterator[CreateTypeStatement] = {
    apply("CreateTypeStatement").map(t => t.asInstanceOf[CreateTypeStatement])
  }
  def tables(): Iterator[CreateTableStatement.RawStatement] = {
    apply("CreateTableStatement\\$RawStatement").map(t => t.asInstanceOf[CreateTableStatement.RawStatement])
  }
}

