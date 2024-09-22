package com.example.Booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private UserDTO user;
    private RoomDTO room;

    public void setId(Long id) {
        this.id = id;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }

    public void setTotalNumOfGuest(int totalNumOfGuest) {
        this.totalNumOfGuest = totalNumOfGuest;
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }
}
