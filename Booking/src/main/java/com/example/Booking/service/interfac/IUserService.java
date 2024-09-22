package com.example.Booking.service.interfac;


import com.example.Booking.dto.LoginRequest;
import com.example.Booking.dto.Response;
import com.example.Booking.models.User;

public interface IUserService {
     Response register(User user);
     Response login(LoginRequest loginRequest);
     Response getAllUsers();
     Response getUserBookingHistory(String userId);
     Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfor(String email);
}
