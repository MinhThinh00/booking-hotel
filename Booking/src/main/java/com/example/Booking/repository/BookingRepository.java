package com.example.Booking.repository;

import com.example.Booking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long > {
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
}
