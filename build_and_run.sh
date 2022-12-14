#!/bin/sh

if [ -z "$API_KEY" ]; then
    echo "Missing variable: API_KEY"
    exit 1
fi

mvn clean package

if [ ! $? -eq 0 ]; then
    echo "[ERROR] Build Failed!"
    exit 1
fi

if [ -x "$(command -v docker)" ]; then
    echo "Executing application by using Docker compose"
    docker compose up --build
else
    echo "Docker installation not found. Running using java -jar"
    java -jar ./target/accounts.jar
fi

