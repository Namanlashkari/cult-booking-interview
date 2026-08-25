# How to run & verify

**Requirement: a JDK 17+. That's it.** No Maven, no JUnit, no internet. The
test framework is a handful of plain `.java` files in this repo under
`com.cultfit.testkit`.

## The easy way — scripts

```bash
# macOS / Linux
./run-tests.sh      # compile + run all tests
./run-app.sh        # compile + run the demo app
```

```bat
REM Windows
run-tests.bat
```

The scripts locate a working JDK on their own (see `find-java.sh`). Some
machines ship a stub `java`/`javac` that just prints "Unable to locate a Java
Runtime" — the scripts test each candidate with `javac -version` and skip any
that fails, so you normally don't set anything. To force a specific JDK:

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk ./run-tests.sh
```

## The manual way — raw commands

The scripts just wrap plain `javac` / `java`. If your `javac` is a working one
(`javac -version` prints a version), these run as-is; otherwise prefix each with
a real JDK, e.g. `/opt/homebrew/opt/openjdk/bin/javac ...`:

```bash
# 1. Compile main + test sources into ./out
javac -d out $(find src/main/java src/test/java -name '*.java')

# 2. Run the test suite through the in-repo runner
java -cp out com.cultfit.testkit.TestRunner \
  com.cultfit.service.BookingServiceTest \
  com.cultfit.service.PricingServiceTest \
  com.cultfit.service.ScheduleServiceTest

# 3. Run the demo app
java -cp out com.cultfit.App
```

### Running a single test class

The runner takes any list of test class names, so to focus on one module:

```bash
java -cp out com.cultfit.testkit.TestRunner com.cultfit.service.PricingServiceTest
```

## What the test kit is

`src/test/java/com/cultfit/testkit/` contains four small files:

| File            | Role                                                        |
|-----------------|-------------------------------------------------------------|
| `Test.java`     | `@Test` annotation (marks a test method)                    |
| `BeforeEach.java` | `@BeforeEach` annotation (setup before every test)        |
| `Assert.java`   | `assertEquals`, `assertTrue`, `assertFalse`, `assertThrows` |
| `TestRunner.java` | discovers `@Test` methods, runs them, prints PASS/FAIL    |

The names match JUnit on purpose — the skill transfers. If you later add JUnit
and Maven/Gradle, swapping the imports is mechanical. We kept it dependency-free
so the exercise runs anywhere, even with no network.

## Expected results

- **Shipped tests against the shipped (buggy) code:** all green (7 passed).
- After you fix a bug, the matching weak test turns **red** — that's your
  signal to also correct the test that was asserting the old behavior.
- The runner exits non-zero when any test fails, so scripts/CI can detect it.
