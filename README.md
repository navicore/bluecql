[![Build Status](https://travis-ci.org/navicore/bluecql.svg?branch=master)](https://travis-ci.org/navicore/bluecql)

# BlueCQL

A tool to generate an HTTP REST server from Cassandra CQL statements.

Resulting generated server code is written in Scala and uses [spray-io](http://spray.io/) for HTTP and JSON and [Phantom](https://github.com/outworkers/phantom) for Cassandra.

  * consumes CQL
  * generates Scala source code
  * generates a standalone working sbt project
  * generates API documentation in an [API Blueprint](https://apiblueprint.org/) specification

## STATUS

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

*UNDER CONSTRUCTION*

```
sbt "run-main onextent.bluecql.Main --file tmp/iot.cql --package onextent.my.iot" && cd out && sbt test && cd ..
```

## NOTES

* intellij 2016.2 can't find the `target/scala-2.10/src_managed/main/antlr4` src folder (extremely annoying, big fail to have to edit intellij config).
  1. go to file/projectstructure
  2. remove the /target dir from the "exclude folder" on right
  3. add `target/scala-2.10/src_managed/main/antlr4` 

some nice cassandra dev commands
```
docker run -p 9042:9042 --name dse -d luketillman/datastax-enterprise:5.0.1
```
```
docker exec -it some-dse cqlsh "-e describe keyspaces;"
```
--------------

*Credit for the antlr4 grammar file to [Stuart Gunter](https://github.com/stuartgunter/cql-grammar).*

