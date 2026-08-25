package com.cultfit.service;

import com.cultfit.model.Booking;
import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.repository.BookingRepository;

public class BookingService {

    private final BookingRepository repository;
    private final PricingService pricingService;

    public BookingService(BookingRepository repository, PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    public Booking book(Member member, FitnessClass fitnessClass) {
        if (repository.exists(member.getId(), fitnessClass.getId())) {
            throw new IllegalStateException("Member already booked into this class");
        }

        int alreadyBooked = repository.countByClassId(fitnessClass.getId());

        // FIX: reject when the class is at OR over capacity (>= not >).
        if (alreadyBooked >= fitnessClass.getCapacity()) {
            throw new IllegalStateException("Class is full");
        }

        int charged = pricingService.priceFor(member, fitnessClass);
        Booking booking = new Booking(member.getId(), fitnessClass.getId(), charged);
        repository.save(booking);
        return booking;
    }

    public int seatsRemaining(FitnessClass fitnessClass) {
        return fitnessClass.getCapacity() - repository.countByClassId(fitnessClass.getId());
    }
}
