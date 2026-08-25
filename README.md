# Cult Booking

A JDK-only fitness-studio booking exercise. Members book classes. Tests are green. The tickets are not.

[![CI](https://github.com/Namanlashkari/cult-booking-interview/actions/workflows/ci.yml/badge.svg)](https://github.com/Namanlashkari/cult-booking-interview/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-c026d3.svg)](LICENSE)

## Layers

```
com.cultfit
├── model        Member, FitnessClass, Booking, MembershipTier
├── repository   BookingRepository (in-memory)
└── service      BookingService, PricingService, ScheduleService
```

No Maven. No JUnit jar. The runner under `com.cultfit.testkit` uses the same `@Test` / `@BeforeEach` / `assertEquals` names as JUnit.

## The job

Read [`TICKETS.md`](TICKETS.md). Reproduce a complaint with a failing test. Fix the service. Then fix any test that was pinning the old bug.

```bash
./run-app.sh      # daily ops report — look for [MISMATCH]
./run-tests.sh    # compile + suite
```

Need a specific JDK: `JAVA_HOME=/path/to/jdk ./run-tests.sh`.

More detail: [`HOW_TO_RUN.md`](HOW_TO_RUN.md) · interviewer notes: [`INTERVIEWER_GUIDE.md`](INTERVIEWER_GUIDE.md)

## What this is for

Collaborative interview work: reading existing code, using tests as proof, talking through tradeoffs. Syntax lookups are allowed.

## License

MIT
