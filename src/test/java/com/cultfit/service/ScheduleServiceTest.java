package com.cultfit.service;

import com.cultfit.model.FitnessClass;
import com.cultfit.testkit.Test;

import java.time.LocalTime;

import static com.cultfit.testkit.Assert.assertTrue;

/**
 * Tests for {@link ScheduleService}. These all currently pass.
 */
class ScheduleServiceTest {

    private final ScheduleService scheduleService = new ScheduleService();

    private FitnessClass cls(String id, int startHour, int endHour) {
        return new FitnessClass(
                id, id,
                LocalTime.of(startHour, 0), LocalTime.of(endHour, 0),
                10, 1000);
    }

    @Test
    void classesThatClearlyOverlapAreDetected() {
        FitnessClass a = cls("a", 10, 12);
        FitnessClass b = cls("b", 11, 13);
        assertTrue(scheduleService.overlaps(a, b));
    }

    @Test
    void backToBackClasses() {
        FitnessClass earlier = cls("earlier", 10, 11);
        FitnessClass later = cls("later", 11, 12);
        assertTrue(scheduleService.overlaps(earlier, later));
    }
}
