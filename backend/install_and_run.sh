#!/bin/bash

BASE=`dirname "$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"`

BUILD_FILENAME="marketplace-0.0.1-SNAPSHOT.jar"
DEST_FILENAME="marketplace.jar"

cd "$BASE/backend"

apt-get update
apt-get install maven docker.io docker-compose -y
mvn clean install

cp "$BASE/backend/target/$BUILD_FILENAME" "$BASE/docker/$DEST_FILENAME"

cd "$BASE/docker"

docker build -t daw-team-11/marketplace .

docker-compose up -d
