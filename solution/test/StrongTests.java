package com.cultfit.service;

import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.model.MembershipTier;
import com.cultfit.repository.BookingRepository;
import com.cultfit.testkit.Test;

import java.time.LocalTime;

import static com.cultfit.testkit.Assert.assertEquals;
import static com.cultfit.testkit.Assert.assertFalse;
import static com.cultfit.testkit.Assert.assertThrows;

/**
 * The strengthened tests a strong candidate would write. Each pins the REAL
 * business rule that the weak tests glossed over. These should FAIL on the
 * buggy code and PASS once the code is fixed.
 */
class StrongTests {

    private FitnessClass twoSeat() {
        return new FitnessClass("spin", "Spin", LocalTime.of(9, 0), LocalTime.of(10, 0), 2, 1000);
    }

    private Member reg(String id) {
        return new Member(id, id, MembershipTier.REGULAR);
    }

    @Test
    void bug1_thirdBookingIntoTwoSeatClassIsRejected() {
        BookingRepository repo = new BookingRepository();
        BookingService svc = new BookingService(repo, new PricingService());
        FitnessClass cls = twoSeat();
        svc.book(reg("a"), cls);
        svc.book(reg("b"), cls);
        assertThrows(IllegalStateException.class, () -> svc.book(reg("c"), cls),
                "class of capacity 2 must reject the 3rd member");
    }

    @Test
    void bug2_eliteMemberGetsFiftyPercentOff() {
        PricingService pricing = new PricingService();
        Member elite = new Member("e", "Eli", MembershipTier.ELITE);
        FitnessClass cls = new FitnessClass("c", "C", LocalTime.of(8, 0), LocalTime.of(9, 0), 10, 2000);
        assertEquals(1000, pricing.priceFor(elite, cls),
                "ELITE = 50% off, so 2000 -> 1000");
    }

    @Test
    void bug3_backToBackClassesDoNotOverlap() {
        ScheduleService svc = new ScheduleService();
        FitnessClass earlier = new FitnessClass("e", "e", LocalTime.of(10, 0), LocalTime.of(11, 0), 10, 1000);
        FitnessClass later = new FitnessClass("l", "l", LocalTime.of(11, 0), LocalTime.of(12, 0), 10, 1000);
        assertFalse(svc.overlaps(earlier, later),
                "10-11 and 11-12 only touch at the boundary; they must not conflict");
    }
}
