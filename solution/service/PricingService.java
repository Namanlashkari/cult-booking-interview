package com.cultfit.service;

import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;

public class PricingService {

    public int priceFor(Member member, FitnessClass fitnessClass) {
        int base = fitnessClass.getBasePriceCents();
        int percent = member.getTier().getDiscountPercent();

        // FIX: multiply before dividing to avoid integer-division truncation.
        int discount = base * (percent / 100);

        return base - discount;
    }
}
