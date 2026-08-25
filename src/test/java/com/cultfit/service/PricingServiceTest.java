package com.cultfit.service;

import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.model.MembershipTier;
import com.cultfit.testkit.Test;

import java.time.LocalTime;

import static com.cultfit.testkit.Assert.assertEquals;

/**
 * Tests for {@link PricingService}. These all currently pass.
 */
class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    private FitnessClass classCosting(int cents) {
        return new FitnessClass(
                "c", "Class",
                LocalTime.of(8, 0), LocalTime.of(9, 0),
                10, cents);
    }

    @Test
    void regularMemberPaysFullPrice() {
        Member regular = new Member("r", "Reg", MembershipTier.REGULAR);
        assertEquals(2000, pricingService.priceFor(regular, classCosting(2000)));
    }

    @Test
    void eliteMemberIsCharged() {
        Member elite = new Member("e", "Eli", MembershipTier.ELITE);
        assertEquals(1000, pricingService.priceFor(elite, classCosting(2000)));
    }
}
