#!/bin/sh
unset JAVA_TOOL_OPTIONS
exec java -Xmx96m -Xms32m -Xss256k -XX:+UseSerialGC \
  -Djava.security.egd=file:/dev/./urandom \
  -jar /app/app.jar
