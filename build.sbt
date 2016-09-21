scalaVersion := "2.10.6"

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
      //"org.apache.cassandra" % "cassandra-all" % "2.2.1",
      "org.apache.cassandra" % "cassandra-all" % "3.7",
      "org.rogach" %% "scallop" % "2.0.1",  //Option parser
      // -- testing --
      "org.scalatest" %% "scalatest" % "2.2.1" % "test"
    )

mainClass in assembly := Some("onextent.bluecql.Main")
assemblyJarName in assembly := s"bluecql.jar"

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("org", "slf4j", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// scala lint
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle
lazy val testScalastyle = taskKey[Unit]("testScalastyle")
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
(test in Test) <<= (test in Test) dependsOn testScalastyle

// antlr
antlr4Settings
antlr4PackageName in Antlr4 := Some("onextent.bluecql.antlr4")
//javaSource in Antlr4 := (javaSource in Antlr4).value / "antlr4"

