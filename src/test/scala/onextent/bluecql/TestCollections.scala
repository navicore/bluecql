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

import onextent.bluecql.cql.Statements
import org.apache.cassandra.cql3.statements.{CreateKeyspaceStatement, CreateTableStatement, CreateTypeStatement}
import org.scalatest.FlatSpec

class TestCollections extends FlatSpec {

  val path = getClass.getResource("/test.cql").getPath
  private def fixture =
    new {
      val statements = new Statements(path)
      val tables = Statements.tables(path)
      val types = Statements.types(path)
      val keyspaces = Statements.keyspaces(path)
    }

  def size(stuff: Iterator[Any]): Int = {
    stuff.foldLeft(0)((sum,_) => sum + 1)
  }

  it should "get each parsed stmt" in {
    val f = fixture
    for (stmt <- f.statements)
      println(stmt)
  }

  it should "get separate each parsed stmt" in {
    val f = fixture
    for (stmt <- f.statements)
      stmt match {
        case ks: CreateKeyspaceStatement => println(s"keyspace $ks")
        case tp: CreateTypeStatement => println(s"type $tp")
        case tb: CreateTableStatement => println(s"table $tb")
        case raw: CreateTableStatement.RawStatement => println(s"raw table $raw")
        case _ => println(s"other ${stmt}")
      }
  }

  it should "get separate each parsed table stmt" in {
    val f = fixture
    for (stmt <- f.tables)
      stmt match {
        case raw: CreateTableStatement.RawStatement => println(s"tble raw table $raw")
        case _ => println(s"tble other ${stmt}")
      }
  }

  it should "get keyspace name" in {
    val f = fixture
    assert(f.keyspaces.next().keyspace() == "mykeyspace")
  }

  it should "get size" in {
    val f = fixture
    assert(size(f.statements) == 4)
    assert(size(f.keyspaces) == 1)
    assert(size(f.types) == 1)
    assert(size(f.tables) == 2)
  }
}

