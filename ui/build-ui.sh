#!/usr/bin/env bash

docker build -t azwickey/fortune-ui .
docker push azwickey/fortune-ui:latest
