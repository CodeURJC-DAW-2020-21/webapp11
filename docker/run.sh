#!/bin/bash

BASE=`dirname "$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"`

# 1. BUILD THE APPLICATION

cd "$BASE/backend"

# Ensure we have the required dependencies installed

apt-get update
apt-get install maven docker.io docker-compose -y

# Compile and create the application executable

mvn clean install

BUILD_FILENAME="$BASE/backend/target/$BUILD_FILENAME/marketplace-0.0.1-SNAPSHOT.jar"
DEST_FILENAME="$BASE/docker/marketplace.jar"

# Ensure old compiled file does not exist

rm -rf $DEST_FILENAME
cp $BUILD_FILENAME $DEST_FILENAME

# 2. CREATE THE DOCKER IMAGE (PACKAGE THE DB AND APP)

cd "$BASE/docker"

# Build the docker image

docker build -t dawhost/marketplace .

# Login to docker hub

docker login --username=dawhost

# Push the image to docker

docker push dawhost/marketplace
