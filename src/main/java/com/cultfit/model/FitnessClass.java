package com.cultfit.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 * A scheduled fitness class on a given day. Time is modelled as a start/end
 * on a single day (the studio runs one schedule per day).
 */
public class FitnessClass {
    private final String id;
    private final String name;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int capacity;
    private final int basePriceCents;

    public FitnessClass(String id,
                        String name,
                        LocalTime startTime,
                        LocalTime endTime,
                        int capacity,
                        int basePriceCents) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        if (basePriceCents < 0) {
            throw new IllegalArgumentException("basePriceCents must not be negative");
        }
        this.capacity = capacity;
        this.basePriceCents = basePriceCents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getBasePriceCents() {
        return basePriceCents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FitnessClass)) return false;
        FitnessClass that = (FitnessClass) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FitnessClass{id=" + id + ", name=" + name
                + ", " + startTime + "-" + endTime
                + ", capacity=" + capacity + '}';
    }
}
