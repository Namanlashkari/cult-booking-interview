# Cult Booking — Coding Exercise

A small fitness-studio booking app. Members book fitness classes over the
course of a day. It has three layers:

```
com.cultfit
├── model        (Member, FitnessClass, Booking, MembershipTier)
├── repository   (BookingRepository — in-memory, no database)
└── service      (BookingService, PricingService, ScheduleService)  ← business logic
```

**No build tool, no libraries, no internet needed.** All you need is a JDK
(17 or newer). The tiny test framework lives in the repo itself under
`com.cultfit.testkit` — its `@Test` / `@BeforeEach` / `assertEquals` /
`assertThrows` are named to match JUnit, so what you practice here transfers
directly to a real project.

The app compiles and its test suite is **green**. Your job is not to start
from a blank page — it is to work with code that already exists and looks
finished, but that members have complained about.

## The task

The studio has some **support tickets** — real complaints from members and the
front desk. They are in [`TICKETS.md`](TICKETS.md). Your interviewer will point
you at one to start with.

The catch: the tests all pass. Passing tests do not always mean correct code —
a test only proves the code does *what the test asked*, which may not be *what
the business asked*.

To work a ticket:

1. **See it happen.** Run `./run-app.sh` first. It prints a daily operations
   report and flags each reported problem with `[MISMATCH]` next to what the
   studio expected. Find the line your ticket is about.
2. **Reproduce it in a test.** Add or strengthen a test that *fails because of
   the bug*. A failing test is you reproducing the complaint in code.
3. **Fix the code** so your test passes, without breaking the honest tests.
4. **Keep the suite honest.** When you fix the code, a test that was written
   around the *old* behavior may start to fail — that is expected, it was
   pinning the bug. Correct its expectation so it now pins the *right* behavior.
   (Fixing a stale test is the *last* step after your fix, not a shortcut to
   stay green.)

Explore, form a hypothesis, and talk through it as you go.

## What we are looking for

This is a **collaborative** exercise, not a silent timed test. We care about:

- How you read unfamiliar code and reason about intended vs. actual behavior.
- How you use tests to *prove* a bug exists before you change production code.
- How you communicate — ask us questions, think out loud, tell us your plan.
- **You may Google, read docs, or use an AI assistant for syntax.** Real
  engineers do. We are evaluating your reasoning and collaboration, not whether
  you memorized Java. Just tell us what you are looking up and why.

Language is not a barrier here — if Java syntax slows you down, say so and look
it up. We want to see how you think.

## Running it

You need a JDK (17+). Nothing else — no Maven, no downloads.

```bash
# macOS / Linux
./run-app.sh        # run this FIRST — prints the daily ops report with [MISMATCH] flags
./run-tests.sh      # compile everything and run the test suite
```

```bat
REM Windows
run-tests.bat
```

The scripts **find a working JDK automatically** — you should not need to
configure anything. (Some machines ship a fake `java`/`javac` that just errors;
the scripts detect that and skip past it to a real JDK.) If you have several
JDKs and want a specific one, set `JAVA_HOME` and it will be used:
`JAVA_HOME=/path/to/jdk ./run-tests.sh`.

See `HOW_TO_RUN.md` for the raw `javac`/`java` commands the scripts wrap, and
for how to run a single test class.

## Ground rules

- Keep the layering. Business logic belongs in the service classes.
- Small, readable commits with a message explaining the *why* are better than
  one big one.
- If something is ambiguous, ask. Asking good questions is a positive signal.
