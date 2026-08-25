package com.cultfit.model;

/**
 * Membership tiers offered by the studio. Each tier carries a percentage
 * discount that is applied to the base price of a class.
 */
public enum MembershipTier {
    REGULAR(0),
    SILVER(10),
    GOLD(25),
    ELITE(50);

    private final int discountPercent;

    MembershipTier(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
