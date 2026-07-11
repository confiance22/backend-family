#!/bin/bash
set -e

DRIVER_DIR="/app/lib"
DRIVER_JAR="$DRIVER_DIR/postgresql.jar"
DRIVER_URL="https://jdbc.postgresql.org/download/postgresql-42.7.4.jar"

if [ ! -f "$DRIVER_JAR" ]; then
    mkdir -p "$DRIVER_DIR"
    echo "Downloading PostgreSQL driver..."
    curl -sL -o "$DRIVER_JAR" "$DRIVER_URL"
fi

exec java -Dloader.path="$DRIVER_DIR" -jar /app/app.jar
