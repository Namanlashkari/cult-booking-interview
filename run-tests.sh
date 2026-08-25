#!/bin/bash
# Compile and run the whole test suite. Needs ONLY a JDK (17+) — no Maven, no
# JUnit, no network. A working JDK is found automatically (see find-java.sh);
# you can still force one with JAVA_HOME=/path/to/jdk ./run-tests.sh
set -e
cd "$(dirname "$0")"
source ./find-java.sh

OUT=out
rm -rf "$OUT"
mkdir -p "$OUT"

# Compile main + test sources (testkit is under src/test/java).
"$JAVAC" -d "$OUT" $(find src/main/java src/test/java -name '*.java')

# Run every test class through the dependency-free runner.
"$JAVA" -cp "$OUT" com.cultfit.testkit.TestRunner \
  com.cultfit.service.BookingServiceTest \
  com.cultfit.service.PricingServiceTest \
  com.cultfit.service.ScheduleServiceTest
