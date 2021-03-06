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

import onextent.bluecql.Config
import onextent.bluecql.cql.Statements
import org.apache.cassandra.cql3.statements.CreateTableStatement

trait CodeGenerator extends Config {

  def argCnt(stmt: CreateTableStatement.RawStatement): Int = {
    //todo: count valid args for case classes
    1
  }
}

object CodeGenerator extends Config {
  def apply(): Unit = {
    var ks = Statements.keyspaces().next().keyspace()
    CaseCode()
    TableCode()
    DbDirectives(ks)
    RouterCode(ks)
    DbCode(ks)
    MainCode()
    SbtCode(ks)
  }
}

