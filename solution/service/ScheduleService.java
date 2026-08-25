package com.cultfit.service;

import com.cultfit.model.FitnessClass;

public class ScheduleService {

    public boolean overlaps(FitnessClass a, FitnessClass b) {
        // FIX: touching boundaries do NOT overlap — use strict isBefore.
        boolean aStartsBeforeBEnds = a.getStartTime().isBefore(b.getEndTime());
        boolean bStartsBeforeAEnds = b.getStartTime().isBefore(a.getEndTime());
        return aStartsBeforeBEnds && bStartsBeforeAEnds;
    }
}
