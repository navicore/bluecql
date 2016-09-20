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

import java.io.File

import onextent.bluecql.gen.CodeGenerator
import org.apache.log4j.BasicConfigurator
import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory

object Main extends App with Config {

  BasicConfigurator.configure()
  val logger = LoggerFactory.getLogger(Main.getClass.getName)

  object Args extends ScallopConf(args) {
    val outdir = opt[String]("out", descr = "output root directory", required = false, default = Some("./out"))
    val file = opt[String]("file", descr = "CQL input file", required = true)
    val pkg = opt[String]("package", descr = "the name of the Scala package to generate", required = true)
  }

  Args.verify()

  property(OUT_DIR_PROP, Args.outdir())
  property(FILE_PROP, Args.file())
  property(PACKAGE_PROP, Args.pkg())

  CodeGenerator()
}

