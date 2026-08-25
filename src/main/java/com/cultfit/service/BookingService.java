package com.cultfit.service;

import com.cultfit.model.Booking;
import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.repository.BookingRepository;

/**
 * Books members into classes over the course of a day.
 *
 * <p>Business rule: a class has a fixed capacity. Once that many members are
 * booked, the class is full and no further bookings are allowed. A member may
 * not book the same class twice.
 */
public class BookingService {

    private final BookingRepository repository;
    private final PricingService pricingService;

    public BookingService(BookingRepository repository, PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    /**
     * Attempts to book {@code member} into {@code fitnessClass}.
     *
     * @return the created {@link Booking}
     * @throws IllegalStateException if the member is already booked or the
     *                               class is full
     */
    public Booking book(Member member, FitnessClass fitnessClass) {
        if (repository.exists(member.getId(), fitnessClass.getId())) {
            throw new IllegalStateException("Member already booked into this class");
        }

        int alreadyBooked = repository.countByClassId(fitnessClass.getId());

        // Reject the booking if the class is already full.
        if (alreadyBooked > fitnessClass.getCapacity()) {
            throw new IllegalStateException("Class is full");
        }

        int charged = pricingService.priceFor(member, fitnessClass);
        Booking booking = new Booking(member.getId(), fitnessClass.getId(), charged);
        repository.save(booking);
        return booking;
    }

    /** How many seats remain in the class. */
    public int seatsRemaining(FitnessClass fitnessClass) {
        return fitnessClass.getCapacity() - repository.countByClassId(fitnessClass.getId());
    }
}
