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

import java.io.{File, PrintWriter}

import org.apache.cassandra.cql3.statements.{CFPropDefs, CreateTableStatement}

object Cql {

  def pkgdir(pkg: String): String = {
    val regex = "\\.".r
    val pkgpath = regex.replaceAllIn(pkg, "/")
    val pkgdir = s"out/src/main/scala/${pkgpath}"
    new File(s"${pkgdir}").mkdirs()
    pkgdir
  }

  def mkTableCode(statements: Iterator[CreateTableStatement.RawStatement], pkg: String, pdir: String): Unit = {
    for (stmt <- statements) {
      val dbfile = s"${pdir}/${stmt.columnFamily()}.scala"
      val code = TableCode(pkg, stmt)
      new PrintWriter(dbfile) { write(s"$code\n"); close }
    }
  }
  def mkDbCode(ks: String, statements: Iterator[CreateTableStatement.RawStatement], pkg: String, pdir: String): Unit = {
    val dbfile = s"${pdir}/Db.scala"
    val code = DbCode(ks, pkg, statements)
    new PrintWriter(dbfile) { write(s"$code\n"); close }
  }

  def mkCaseCode(statements: Iterator[CreateTableStatement.RawStatement], pkg: String, pdir: String): Unit = {
    val dbfile = s"${pdir}/DbData.scala"
    new PrintWriter(dbfile) { write("db data file contents\n"); close }
  }

  def process(filepath: String, pkg: String): Unit = {

    var ks = Statements.keyspaces(filepath).next().keyspace()
    var pdir = pkgdir(pkg)
    mkDbCode(ks, Statements.tables(filepath), pkg, pdir)
    mkCaseCode(Statements.tables(filepath), pkg, pdir)
    mkTableCode(Statements.tables(filepath), pkg, pdir)
  }
}

