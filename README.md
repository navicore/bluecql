# BlueCQL

A tool to generate an HTTP REST server from Cassandra CQL statements.

Resulting generated server code is written in Scala and uses [spray-io](http://spray.io/) for HTTP and JSON and [Phantom](https://github.com/outworkers/phantom) for Cassandra.

  * the tool consumes CQL
  * the tool generates Scala source code
  * the ouput is standalone working sbt project that accesses your Cassandra DB via REST
  * the tool generates API documentation in an [API Blueprint](https://apiblueprint.org/) specification

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

some nice cassandra dev commands
```
docker run -p 9042:9042 --name dse -d luketillman/datastax-enterprise:5.0.1
```
```
docker exec -it some-dse cqlsh "-e describe keyspaces;"
```
--------------

*Credit for the antlr4 grammar file to [Stuart Gunter](https://github.com/stuartgunter/cql-grammar).*

