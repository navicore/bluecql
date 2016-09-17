CREATE KEYSPACE mykeyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = false;

CREATE TYPE mykeyspace.mygeo (
    latitude double,
    longitude double
);

CREATE TABLE mykeyspace.mytable (
    name text,
    loc frozen<mygeo>
);

CREATE TABLE mykeyspace.myothertable (
    name text
    amt Int
);
