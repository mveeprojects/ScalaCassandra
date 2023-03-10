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

### Temporary while working on custom cassandra image (keyspace and table defined)

1. Build the custom cassandra image `docker build ./cassandra -t mycassandra`
2. Then run everything in the normal way`./sbt dockerComposeUp`

## Running all tests via docker

The following sbt command will start the required docker containers, run the tests (with a readiness check waiting for the app to become ready), and then stop and remove all containers.

`./sbt runAllTests`

## HTTP Endpoints

### Return all videos for a user

`curl localhost:8080/videos/<userId>`

### Return first N number of videos from a users' list

`curl localhost:8080/videos/<userId>/<n>`

### Add an item to a users' videos

`curl -X PUT localhost:8080/videos/<userId>/<videoId>`
 
### Remove an item from a users' videos

`curl -X DELETE localhost:8080/videos/<userId>/<videoId>`

## Metrics

Metrics surfaced by Kamon are exposed here -> `http://localhost:9095/metrics`

### Custom Metrics

`cassandra_reachable`
  * value of 1 represents "true", cassandra is reachable, this is the default value on startup
  * code to be added to set this to 0 ("false") in the event cassandra is unreachable 

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

* https://docs.datastax.com/en/developer/java-driver/4.14
* https://www.baeldung.com/cassandra-datastax-java-driver
  * https://github.com/eugenp/tutorials/tree/master/persistence-modules/java-cassandra/src/main/java/com/baeldung/datastax/cassandra
* https://github.com/Tapad/sbt-docker-compose
* https://docs.datastax.com/en/developer/java-driver/3.6/manual/metrics/#metrics-4-compatibility
  * (JMX issue) - justification for creating a cluster using `.withoutJMXReporting`
* https://kamon.io/docs/latest/guides/how-to/start-with-the-kanela-agent/#using-sbt-native-packager
  * https://bintray.com/kamon-io/releases/kanela/1.0.7
* Resources on creating custom Cassandra image
  * https://www.linkedin.com/pulse/creating-cassandra-keyspace-table-docker-start-up-amon-peter
  * https://gist.github.com/derlin/0d4c98f7787140805793d6268dae8440
  * https://stackoverflow.com/questions/75688717/create-tables-automatically-in-dockerised-cassandra-4-x
