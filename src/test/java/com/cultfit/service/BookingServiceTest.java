package com.cultfit.service;

import com.cultfit.model.Booking;
import com.cultfit.model.FitnessClass;
import com.cultfit.model.Member;
import com.cultfit.model.MembershipTier;
import com.cultfit.repository.BookingRepository;
import com.cultfit.testkit.BeforeEach;
import com.cultfit.testkit.Test;

import java.time.LocalTime;

import static com.cultfit.testkit.Assert.assertEquals;
import static com.cultfit.testkit.Assert.assertThrows;
import static com.cultfit.testkit.Assert.assertTrue;

/**
 * Tests for {@link BookingService}. These all currently pass.
 */
class BookingServiceTest {

    private BookingService bookingService;

    private FitnessClass twoSeatClass;

    @BeforeEach
    void setUp() {
        BookingRepository repository = new BookingRepository();
        bookingService = new BookingService(repository, new PricingService());
        twoSeatClass = new FitnessClass(
                "spin", "Spin Class",
                LocalTime.of(9, 0), LocalTime.of(10, 0),
                2, 1000);
    }

    private Member member(String id) {
        return new Member(id, "Member " + id, MembershipTier.REGULAR);
    }

    @Test
    void booksAMemberIntoAClass() {
        bookingService.book(member("a"), twoSeatClass);
        assertEquals(1, bookingService.seatsRemaining(twoSeatClass));
    }

    @Test
    void fillsAllSeatsInAClass() {
        bookingService.book(member("a"), twoSeatClass);
        bookingService.book(member("b"), twoSeatClass);
        assertEquals(0, bookingService.seatsRemaining(twoSeatClass));
    }

    @Test
    void duplicateBookingIsRejected() {
        Member sameMember = member("a");
        bookingService.book(sameMember, twoSeatClass);
        // Booking the SAME member into the SAME class twice must fail. This one
        // is here as a working example of the assertThrows(...) + lambda pattern
        // you can reuse when a rule says "this should not be allowed".
        assertThrows(IllegalStateException.class,
                () -> bookingService.book(sameMember, twoSeatClass));
    }

    @Test
    void thirdBookingIntoFullClass() {
        bookingService.book(member("a"), twoSeatClass);
        bookingService.book(member("b"), twoSeatClass);
        // Both of the 2 seats are now taken. This test documents what book()
        // does with a THIRD member as the code stands today.
        Booking third = bookingService.book(member("c"), twoSeatClass);
        assertTrue(third != null);
    }
}
