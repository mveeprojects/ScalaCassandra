#!/bin/bash

KEYSPACE="testkeyspace"
TABLE="video"

CQL="
CREATE KEYSPACE $KEYSPACE WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE $KEYSPACE;
CREATE TABLE IF NOT EXISTS $TABLE(userid text, videoid text, title TEXT, creationdate TIMESTAMP, PRIMARY KEY (userid, videoid));
"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable - retry later"
  sleep 2
done &

exec /docker-entrypoint.sh