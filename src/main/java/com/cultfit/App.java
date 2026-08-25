package com.cultfit;

import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.model.MembershipTier;
import com.cultfit.repository.BookingRepository;
import com.cultfit.service.BookingService;
import com.cultfit.service.PricingService;
import com.cultfit.service.ScheduleService;

import java.time.LocalTime;

/**
 * A "daily operations report" for the studio. It runs a few real bookings and
 * prints, for each one, what the system actually did next to what the studio
 * expected. Lines flagged [MISMATCH] are things a member or the front desk has
 * complained about — see TICKETS.md.
 *
 * <p>Run it with {@code ./run-app.sh} (Windows: build + {@code java -cp out
 * com.cultfit.App}). This is the first thing to run: it makes the reported
 * problems visible before you open any code. Once you fix a bug, the matching
 * line here should flip from [MISMATCH] to [ok].
 */
public class App {

    public static void main(String[] args) {
        BookingRepository repository = new BookingRepository();
        PricingService pricingService = new PricingService();
        BookingService bookingService = new BookingService(repository, pricingService);
        ScheduleService scheduleService = new ScheduleService();

        // One class, capacity 2, base price 2000 cents (10:00–11:00).
        FitnessClass morningYoga = new FitnessClass(
                "yoga-am", "Morning Yoga",
                LocalTime.of(10, 0), LocalTime.of(11, 0),
                2, 2000);

        // A back-to-back class that starts exactly when Morning Yoga ends.
        FitnessClass hiit = new FitnessClass(
                "hiit-am", "HIIT",
                LocalTime.of(11, 0), LocalTime.of(12, 0),
                20, 3000);

        Member priya = new Member("m1", "Priya", MembershipTier.ELITE);
        Member rahul = new Member("m2", "Rahul", MembershipTier.REGULAR);
        Member sana = new Member("m3", "Sana", MembershipTier.SILVER);

        System.out.println("=== Cult Booking — daily operations report ===");
        System.out.println();

        // --- PRICING -------------------------------------------------------
        System.out.println("[PRICING]  Morning Yoga, base 2000 cents");
        int priyaCharged = bookingService.book(priya, morningYoga).getChargedCents();
        check("  Priya  (ELITE, 50% off)  charged " + priyaCharged + " cents",
                priyaCharged == 1000, "expected 1000");

        int rahulCharged = bookingService.book(rahul, morningYoga).getChargedCents();
        check("  Rahul  (REGULAR, 0% off) charged " + rahulCharged + " cents",
                rahulCharged == 2000, "expected 2000");

        // --- CAPACITY ------------------------------------------------------
        // Priya + Rahul have filled the 2 seats. Sana is a walk-in the front
        // desk tries to squeeze in.
        System.out.println();
        System.out.println("[CAPACITY]  Morning Yoga is full (2 of 2 seats booked)");
        try {
            bookingService.book(sana, morningYoga);
            int seatsLeft = bookingService.seatsRemaining(morningYoga);
            check("  Sana was booked anyway; seats remaining now " + seatsLeft,
                    false, "expected the 3rd booking to be REJECTED (seats never below 0)");
        } catch (IllegalStateException rejected) {
            check("  Sana's booking was rejected: " + rejected.getMessage(),
                    true, "expected the 3rd booking to be rejected");
        }

        // --- SCHEDULE ------------------------------------------------------
        System.out.println();
        System.out.println("[SCHEDULE]  Morning Yoga 10:00–11:00  vs  HIIT 11:00–12:00");
        boolean overlap = scheduleService.overlaps(morningYoga, hiit);
        check("  overlap? " + overlap,
                !overlap, "expected false — they only touch at 11:00, so a member can attend both");
    }

    /** Prints one report line with an [ok] / [MISMATCH] flag and the expectation. */
    private static void check(String actual, boolean ok, String expectation) {
        System.out.println(actual + "   <-- " + expectation + "   " + (ok ? "[ok]" : "[MISMATCH]"));
    }
}
