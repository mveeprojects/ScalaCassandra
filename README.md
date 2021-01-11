# ScalaCassandra

Simple Scala app to learn about Cassandra integration.

## Running the app

### SBT docker compose plugin

To run the application via docker-compose, from the root of the project run the following `./sbt dockerComposeUp`.

### Cassandra in docker, app in IntelliJ

1. Run this from the root of the project `./docker/CassandraVanillaDocker.sh`, wait a few seconds (up to 10/20) for
   Cassandra to become ready.
2. Start the app up in IntelliJ.

## Sources

* https://docs.datastax.com/en/developer/java-driver/4.9
* https://www.baeldung.com/cassandra-datastax-java-driver
    * https://github.com/eugenp/tutorials/tree/master/persistence-modules/java-cassandra/src/main/java/com/baeldung/datastax/cassandra
* https://github.com/Tapad/sbt-docker-compose