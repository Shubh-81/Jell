#!/usr/bin/env bash

set -e # Exit early if any commands fail

(
  cd "$(dirname "$0")" # Ensure compile steps are run within the repository directory
  mvn clean
  mvn -q -B package -DargLine="-XX:+EnableDynamicAgentLoading" -DargLine="-Xlog:disable" -Ddir=/tmp/codecrafters-build-shell-java
)

exec java \
  -jar /tmp/codecrafters-build-shell-java/codecrafters-shell.jar \
  -Dexec.mainClass="NewMain" \
  "$@" \
    2> >(grep -v '^WARNING:')