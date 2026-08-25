package com.cultfit.model;

import java.util.Objects;

/**
 * A confirmed booking of a member into a class, with the price that was
 * charged at the time of booking.
 */
public class Booking {
    private final String memberId;
    private final String classId;
    private final int chargedCents;

    public Booking(String memberId, String classId, int chargedCents) {
        this.memberId = Objects.requireNonNull(memberId, "memberId");
        this.classId = Objects.requireNonNull(classId, "classId");
        this.chargedCents = chargedCents;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getClassId() {
        return classId;
    }

    public int getChargedCents() {
        return chargedCents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return memberId.equals(booking.memberId) && classId.equals(booking.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, classId);
    }

    @Override
    public String toString() {
        return "Booking{member=" + memberId + ", class=" + classId
                + ", charged=" + chargedCents + '}';
    }
}
