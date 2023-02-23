#!/bin/bash

./sbt dockerComposeStop

docker build ./cassandra -t my_cassandra

./sbt dockerComposeUp
