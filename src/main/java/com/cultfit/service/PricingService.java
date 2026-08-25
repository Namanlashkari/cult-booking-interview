package com.cultfit.service;

import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;

/**
 * Calculates the price a member pays for a class.
 *
 * <p>Business rule: the member pays the class base price minus their
 * membership-tier discount. For example, an ELITE member (50% off) booking a
 * class that costs 2000 cents should be charged 1000 cents.
 */
public class PricingService {

    /**
     * Returns the price in cents that {@code member} must pay for
     * {@code fitnessClass}, after applying the member's tier discount.
     */
    public int priceFor(Member member, FitnessClass fitnessClass) {
        int base = fitnessClass.getBasePriceCents();
        int percent = member.getTier().getDiscountPercent();

        // Apply the percentage discount to the base price.
        int discount = base * (percent / 100);

        return base - discount;
    }
}
