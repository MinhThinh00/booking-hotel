package com.example.Booking.service.interfac;

import com.example.Booking.dto.Response;
import com.example.Booking.models.Booking;



public interface IBookingService {
        Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
        Response findBookingByConfirmationCode(String confirmationCode);
        Response getAllBooking();
        Response cancelBooking(Long bookingId);

}
