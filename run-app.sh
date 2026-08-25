#!/bin/bash
# Compile and run the demo app. Needs ONLY a JDK (17+). A working JDK is found
# automatically; force one with JAVA_HOME=/path/to/jdk ./run-app.sh
set -e
cd "$(dirname "$0")"
source ./find-java.sh

OUT=out
rm -rf "$OUT"
mkdir -p "$OUT"

"$JAVAC" -d "$OUT" $(find src/main/java -name '*.java')
"$JAVA" -cp "$OUT" com.cultfit.App
