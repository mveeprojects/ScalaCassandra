#!/bin/bash

CQL="
CREATE KEYSPACE testkeyspace WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};
CREATE TABLE IF NOT EXISTS testkeyspace.video(userid text, videoid text, title TEXT, creationdate TIMESTAMP, PRIMARY KEY (userid, videoid));
"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable - retry later"
  sleep 2
done &

exec /docker-entrypoint.sh