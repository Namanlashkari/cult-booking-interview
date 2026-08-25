package com.cultfit.service;

import com.cultfit.model.FitnessClass;

/**
 * Answers scheduling questions about classes on a single day.
 *
 * <p>Business rule: two classes "overlap" only when they actually share time
 * on the clock. Back-to-back classes that merely touch at a boundary — one
 * ending at 11:00 and the next starting at 11:00 — do NOT overlap; a member
 * could attend both.
 */
public class ScheduleService {

    /**
     * Returns {@code true} if the two classes overlap in time.
     */
    public boolean overlaps(FitnessClass a, FitnessClass b) {
        // They overlap if a starts before b ends AND b starts before a ends.
        boolean aStartsBeforeBEnds = !a.getStartTime().isAfter(b.getEndTime());
        boolean bStartsBeforeAEnds = !b.getStartTime().isAfter(a.getEndTime());
        return aStartsBeforeBEnds && bStartsBeforeAEnds;
    }
}
