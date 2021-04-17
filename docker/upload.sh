#!/bin/bash

# Login to docker hub
docker login --username=dawhost

# Push the image to docker
docker push dawhost/marketplace
