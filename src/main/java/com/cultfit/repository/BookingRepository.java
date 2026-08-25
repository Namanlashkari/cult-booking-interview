package com.cultfit.repository;

import com.cultfit.model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory store of confirmed bookings. No database — everything lives in a
 * list for the duration of the process. This is intentional: the exercise is
 * about application logic, not persistence.
 */
public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public void save(Booking booking) {
        bookings.add(booking);
    }

    /** Returns all bookings for a given class. */
    public List<Booking> findByClassId(String classId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getClassId().equals(classId)) {
                result.add(b);
            }
        }
        return result;
    }

    /** Number of members currently booked into the class. */
    public int countByClassId(String classId) {
        return findByClassId(classId).size();
    }

    public boolean exists(String memberId, String classId) {
        for (Booking b : bookings) {
            if (b.getMemberId().equals(memberId) && b.getClassId().equals(classId)) {
                return true;
            }
        }
        return false;
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }
}
