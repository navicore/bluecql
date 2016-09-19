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

import org.apache.cassandra.cql3.statements.CreateTableStatement

object SbtCode {

  def apply(keyspace: String, pkg: String): Unit = {
    applySbt(keyspace, pkg)
    applyPlugins()
    applyAssembly()
  }

  def applyAssembly(): Unit = {
    val dir = new File(s"out/project/").mkdirs()
    val file = "out/project/assembly.sbt"
    val code =
"""addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
""".stripMargin
    new PrintWriter(file) { write(s"$code\n"); close }
  }

  def applyPlugins(): Unit = {
    val dir = new File(s"out/project/").mkdirs()
    val file = "out/project/plugins.sbt"
    val code =
"""addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")
""".stripMargin
    new PrintWriter(file) { write(s"$code\n"); close }
  }

  def applySbt(keyspace: String, pkg: String): Unit = {
    val root = s"out/"
    val file = s"${root}/build.sbt"
    val code = s"""scalaVersion := "2.10.6"
val akka = "2.3.15"
val PhantomVersion = "1.22.0"
val spray = "1.3.2"

resolvers ++= Seq(
 "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype repo"                    at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases"                at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots"               at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging"                 at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies ++=
    Seq(
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "log4j" % "log4j" % "1.2.17",
      // -- cassandra --
      "com.websudos" %% "phantom-dsl" % PhantomVersion exclude("com.chuusai", "shapeless_2.10"),
      // -- Akka --
      "com.typesafe.akka" %% "akka-actor" % akka,

      // -- Spray --
      "io.spray" %% "spray-routing-shapeless2" % spray,
      "io.spray" %% "spray-client" % spray,
      "io.spray" %% "spray-json" % spray,

      // -- config --
      "org.rogach" %% "scallop" % "2.0.1",  //Option parser
      // -- testing --
      "org.scalatest" %% "scalatest" % "2.2.1" % "test"
    )

mainClass in assembly := Some("${pkg}.Main")
assemblyJarName in assembly := "$keyspace.jar"

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("io",  "netty", xs @ _*) => MergeStrategy.last
  case PathList("org", "slf4j", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
""".stripMargin
    new PrintWriter(file) { write(s"$code\n"); close }
  }
}

