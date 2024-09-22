package com.example.Booking.dto;


import com.example.Booking.models.Booking;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    private String email;

    public  void setId(Long id) {
        this.id = id;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }

    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookings= new ArrayList<>();
}
