# Support tickets

These are real reports the studio has received. Each one points at one area of
the app. Your interviewer will tell you which ticket to pick up.

Work a ticket the way an engineer would:

1. **Reproduce it.** Run `./run-app.sh` and find the line the ticket is about.
2. **Prove it with a test.** Add or strengthen a test that fails *because* of
   the bug — a failing test is you reproducing the complaint in code.
3. **Fix the code** so your test passes.
4. **Keep the suite honest.** When you fix the code, a test that was written
   around the *old* behavior may start to fail. That is expected — correct its
   expectation so it now pins the *right* behavior. (Fixing a stale test is the
   last step, not a shortcut to stay green.)

Talk us through it as you go — questions, hypotheses, dead ends. That is the
part we care most about.

---

## TICKET-101 — Pricing

> **From:** Priya (ELITE member)
> **Subject:** I wasn't given my discount
>
> I'm on the ELITE tier, which is supposed to be 50% off. I booked Morning Yoga
> (list price 2000) and was charged the full 2000. My friend Sana (SILVER, 10%
> off) says the same thing happened to her. Are discounts working at all?

**Repro:** book an ELITE member into a 2000-cent class.
**Expected:** charged 1000.  **Actual:** charged 2000.

---

## TICKET-102 — Capacity

> **From:** Front desk / ops
> **Subject:** A class was overbooked
>
> Morning Yoga only has 2 spots. Both were already taken, but when I tried to
> add a walk-in the system let me book them anyway. Afterwards the class showed
> a negative number of seats remaining. It should have stopped me.

**Repro:** fill a 2-seat class, then try to book a 3rd member.
**Expected:** the 3rd booking is rejected; seats remaining never goes below 0.
**Actual:** the 3rd booking is accepted.

---

## TICKET-103 — Schedule

> **From:** Rahul (member)
> **Subject:** App says two classes clash when they don't
>
> I wanted to do Morning Yoga (10:00–11:00) and then HIIT (11:00–12:00) right
> after. The app says they conflict, so it won't let me plan both. They don't
> overlap — one ends exactly when the other starts.

**Repro:** ask whether a 10:00–11:00 class overlaps an 11:00–12:00 class.
**Expected:** they do **not** overlap.  **Actual:** reported as overlapping.
