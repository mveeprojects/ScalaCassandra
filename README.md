# ScalaCassandra

Simple Scala app to learn about Cassandra integration.

## Running the app

### SBT docker compose plugin

To run the application and Cassandra together via docker-compose, from the root of the project run the
following `./sbt dockerComposeUp`.

### Cassandra in docker, app in IntelliJ

1. Run this from the root of the project `./docker/CassandraVanillaDocker.sh`, wait a few seconds (up to 10/20) for
   Cassandra to become ready.
2. Start the app up in IntelliJ.

## HTTP Endpoints

### Return all videos for a user

`curl localhost/videos/<userId>`

### Return first N number of videos from a users' list

`curl localhost/videos/<userId>/<n>`

### Add an item to a users' videos

`curl -X PUT localhost/videos/<userId>/<videoId>`

## Cqlsh

### To access the command line (`cqlsh`) via docker

`docker exec -it cassandra_container_name_or_id cqlsh`

### To view all keyspaces

`describe keyspaces`

### To switch keyspace

`use keyspace_name;`

### To list all tables

`describe tables;`

### To select all records from a table

`select * from table_name;`

## Sources

* https://docs.datastax.com/en/developer/java-driver/4.9
* https://www.baeldung.com/cassandra-datastax-java-driver
    * https://github.com/eugenp/tutorials/tree/master/persistence-modules/java-cassandra/src/main/java/com/baeldung/datastax/cassandra
* https://github.com/Tapad/sbt-docker-compose
* https://docs.datastax.com/en/developer/java-driver/3.6/manual/metrics/#metrics-4-compatibility
    * (JMX issue) - justification for creating a cluster using `.withoutJMXReporting`
* https://getquill.io/
