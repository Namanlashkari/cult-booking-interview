# Interviewer Guide — Cult Booking Exercise

**Do not give this file to candidates.** It contains the planted bugs, the
expected fixes, the hint ladder, and how to run the exercise.

## How this exercise is meant to run

This is a **live, interviewer-in-the-loop** exercise for college interns. It is
not a take-home. You are watching them debug and reason in real time — that
observation *is* the measurement, so most of the signal comes from the
conversation, not the final diff.

- **Assign one ticket per candidate** (see `TICKETS.md`). One ticket = one bug
  in one service. Each bug is a one-line fix and should be solvable in **about
  10 minutes** including the conversation.
- Candidates are explicitly allowed to Google / use an AI assistant for syntax.
  That's fine and expected — the anti-paste probes below keep the signal honest.
- Start every session by running `./run-app.sh` together (see below). It prints
  a daily ops report that flags the reported problem with `[MISMATCH]`, so the
  candidate *sees* the bug before opening any code.

**Dependencies: none.** The project needs only a JDK (17+). No Maven, no JUnit,
no network — the test framework is a few plain files in `com.cultfit.testkit`
whose API mirrors JUnit. This is deliberate so it runs on any laptop offline.

## Which ticket to give whom (difficulty)

| Ticket | Bug | Service | Difficulty | Give to |
|--------|-----|---------|------------|---------|
| TICKET-101 | Integer-division discount | PricingService | **Easy / warm-up** | anyone; nervous candidates |
| TICKET-102 | Off-by-one capacity | BookingService | **Medium** | most interns |
| TICKET-103 | Inclusive overlap boundary | ScheduleService | **Harder** | stronger / more confident candidates |

If you hand over the *whole* repo instead, tell them to start with TICKET-101 —
it has the most dramatic symptom and a nameable concept (integer division), so
they build confidence and learn the loop before the subtler overlap bug.

## The three planted bugs

### Bug 1 — Integer-division discount (PricingService) · TICKET-101 · easy

- **File:** `src/main/java/com/cultfit/service/PricingService.java`
- **Line:** `int discount = base * (percent / 100);`
- **Symptom:** `percent / 100` is integer division. For every tier below 100%
  it evaluates to `0`, so the discount is always `0` — **no one is ever charged
  less**, regardless of tier.
- **Fix:** multiply before dividing: `int discount = (base * percent) / 100;`
- **Weak test that hides it:** `PricingServiceTest.eliteMemberIsCharged` asserts
  an ELITE member pays the *full* `2000` (should be `1000`). After the fix this
  test fails with a clean `expected <2000> but was <1000>` diff — they must
  correct the expectation to `1000`.
- **Strong test they should write:** ELITE (50% off) on a 2000 class is charged
  1000. Bonus: SILVER (10%) and GOLD (25%).
- **Hint ladder** (only if stuck; note how many rungs they needed):
  1. "Run the app — does the charge for Priya look right?"
  2. "In `priceFor`, what does `percent / 100` evaluate to when `percent` is 50
     and both are `int`s?"
  3. Point at the line: multiply before you divide.

### Bug 2 — Off-by-one capacity (BookingService) · TICKET-102 · medium

- **File:** `src/main/java/com/cultfit/service/BookingService.java`
- **Line:** the capacity guard `if (alreadyBooked > fitnessClass.getCapacity())`
- **Symptom:** a class of capacity *N* accepts *N+1* bookings; `seatsRemaining`
  can go negative.
- **Fix:** `>` → `>=`.
- **Weak test that hides it:** `BookingServiceTest.thirdBookingIntoFullClass`
  books a 3rd member into a 2-seat class and asserts the returned `Booking` is
  non-null — i.e. it documents that overbooking *succeeds*, which is the bug.
- **⚠ Read this before the session:** after the fix (`>=`), the third `book()`
  call **throws `IllegalStateException("Class is full")`** — so the weak test
  fails by *throwing at the `book()` line*, not with a clean assertion diff.
  A nervous candidate may read "Class is full" as "I broke booking" and try to
  revert their correct fix. **Coach, don't penalize:** ask them what the ticket
  said *should* happen to the 3rd booking. The correct move is to rewrite the
  stale test to expect rejection (`assertThrows`).
- **Note on the smell's location:** the bug is in the guard in `book()`, but a
  literal-minded candidate might try to "fix" the negative number by clamping
  `seatsRemaining` with `Math.max(0, …)`. That hides the symptom and leaves
  overbooking intact — probe for root cause vs. symptom.
- **Strong test they should write:** third booking into a 2-seat class throws
  `IllegalStateException`. There's a working `assertThrows` + lambda template in
  the shipped `duplicateBookingIsRejected` test they can copy.
- **Hint ladder:**
  1. "Run the app — the front desk booked a 3rd person into a 2-seat class.
     Trace what happens with capacity 2 and 3 bookings."
  2. "Look at the comparison in the capacity guard. When `alreadyBooked` equals
     the capacity, is the class full yet?"
  3. Point at the line: `>` should be `>=`.

### Bug 3 — Inclusive overlap boundary (ScheduleService) · TICKET-103 · harder

- **File:** `src/main/java/com/cultfit/service/ScheduleService.java`
- **Line:** both boundary checks use `!isAfter(...)` (i.e. `<=` / `>=`).
- **Symptom:** back-to-back classes (10–11 and 11–12) that only *touch* at a
  boundary are reported as overlapping, so a member could never book both.
- **Fix:** use strict `isBefore(...)` for both comparisons.
- **Weak test that hides it:** `ScheduleServiceTest.backToBackClasses` asserts
  the touching classes *do* overlap (`assertTrue`). After the fix it fails; they
  flip it to `assertFalse`.
- **Strong test they should write:** 10–11 and 11–12 do **not** overlap
  (`assertFalse`); genuinely overlapping classes (10–12 and 11–13) still do.
- **Hint ladder:**
  1. "Run the app — it says Yoga (10–11) and HIIT (11–12) overlap. Do they?"
  2. "Think of a class as the half-open interval `[start, end)` — the end
     instant belongs to the next class. Does `!isAfter` treat the boundary that
     way?"
  3. Point at the line: `!x.isAfter(y)` includes equality; you want strict
     `x.isBefore(y)`.

## Solution reference

Corrected source for all three services and strengthened tests are in
`solution/` (keep it out of the candidate copy). Diffs:

```java
// PricingService
- int discount = base * (percent / 100);
+ int discount = (base * percent) / 100;

// BookingService
- if (alreadyBooked > fitnessClass.getCapacity()) {
+ if (alreadyBooked >= fitnessClass.getCapacity()) {

// ScheduleService
- boolean aStartsBeforeBEnds = !a.getStartTime().isAfter(b.getEndTime());
- boolean bStartsBeforeAEnds = !b.getStartTime().isAfter(a.getEndTime());
+ boolean aStartsBeforeBEnds = a.getStartTime().isBefore(b.getEndTime());
+ boolean bStartsBeforeAEnds = b.getStartTime().isBefore(a.getEndTime());
```

## Verified behavior (what "correct" looks like)

Checked by compiling the real files and running each combination:

| Tests               | Against buggy code | Against fixed code |
|---------------------|--------------------|--------------------|
| Shipped (weak) tests | **8 / 8 pass**     | 3 flip to **FAIL** (discount, capacity, overlap) |
| Strengthened tests   | **3 / 3 FAIL**     | **3 / 3 pass**     |

So the intended journey per bug is: *run the app and see the `[MISMATCH]` →
write/​correct a test that fails → fix the one-line bug → update the weak test
that asserted the old behavior → all green.*

Note the capacity weak test fails by *throwing* after the fix (see the ⚠ above),
while the discount and overlap weak tests fail with clean assertion diffs.

## The "red after my fix" moment — say this out loud

Tell the candidate up front: **"When you fix the code, a shipped test may turn
red. That failure is the signal you're right — it was pinning the old, wrong
behavior. Your job is to correct its expectation, not to undo your fix."** This
prevents the most common failure mode (a correct candidate reverting a correct
one-line fix because red = "I broke it").

## What to probe / signals

Score three independent things, not TDD ceremony:

**1. Found the wrong behavior**
- Located the defect and can point at the responsible line.
- Recognizes the symptom is business nonsense (full-price ELITE / overbooked
  class / back-to-back "conflict"), not an arbitrary value.

**2. Proved it**
- Reproduced the bug — ran the app and/or wrote a test that fails *because of*
  the bug — before or alongside changing production code.

**3. Explained why**
- Articulates the root cause: integer truncation, off-by-one at the boundary,
  half-open-interval reasoning for overlaps.
- Communicates: states a hypothesis, asks clarifying questions, narrates,
  responds to nudges (track how many hint rungs they needed).
- Keeps the fix in the right layer; fixes the cause, not the symptom.

**On editing a test's expected value — this is neutral, not automatically weak.**
Correcting a stale assertion to the *right* value after fixing the code is
exactly what the exercise asks for. Only flag it as weak if they change it to
match *buggy* output just to stay green, or do it *instead* of fixing the code.
Distinguish intent, not the mechanical edit.

**Genuinely weak signals**
- Changes a test's expected value to the buggy output to keep the suite green,
  with no code fix.
- Hides the symptom instead of fixing the cause (e.g. clamps `seatsRemaining`).
- Silence — doesn't narrate reasoning even when prompted.
- Gets stuck on Java syntax and neither looks it up nor asks (we encourage both).

## Anti-paste probe (do this even if they solved it fast)

Because a candidate can paste a service file into an LLM, verify understanding
with a *live, on-the-spot* question they can't have pre-fetched:

- **Hand-compute:** "Before you run it — what does this print for GOLD tier
  (25% off) on a 999-cent class?" Correct reasoning: `(999 * 25) / 100 = 249`
  discount → charged **750**. A paster stumbles here; note also whether they
  spot that the correct fix still *truncates* the fractional cent (249.75 → 249)
  and whether they ask how rounding should work — that's a strong-collaborator
  tell (the spec deliberately doesn't say).
- **Predict then run:** invent a fresh input, ask them to predict the output,
  then run it together.

## Follow-up questions to ask live

- "What other test cases would you add for this rule?" (capacity 1; a class
  fully inside another for overlap; every tier for pricing). *Note:* identical
  start/end times **cannot** be constructed — `FitnessClass`'s constructor
  requires `endTime` strictly after `startTime` — so don't suggest that one; if
  the candidate proposes it, the constructor throwing is correct behavior, not a
  bug.
- "How would you prevent this class of bug in a real codebase?" (boundary tests,
  property tests, money as a dedicated type, a `>=` review checklist).

## Handing over a single module in isolation

`PricingService` and `ScheduleService` are self-contained and can be given
alone. **`BookingService` cannot** — its constructor requires a `PricingService`,
so it won't compile without it, and a candidate reading `PricingService` may
also notice the pricing bug. If you want a clean TICKET-102-only session, either
give the whole `service/` package (and just assign the capacity ticket) or hand
over a fixed `PricingService` alongside it.

## Running (any machine, no network)

```bash
./run-app.sh            # daily ops report — run this WITH the candidate first
./run-tests.sh          # all tests   (JAVA_HOME=/opt/homebrew/opt/openjdk ./run-tests.sh if javac is a stub)
run-tests.bat           # Windows
```

To verify the solution yourself: temporarily copy the fixed services from
`solution/service/` over `src/main/java/com/cultfit/service/`, and/or add
`solution/test/StrongTests.java` into `src/test/java/com/cultfit/service/` and
name it in the runner. See `HOW_TO_RUN.md` for raw `javac`/`java` commands.
