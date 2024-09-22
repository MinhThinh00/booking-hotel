package com.example.Booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is reqiured")
    private String email;

    @NotBlank(message = "password is reqiured")
    private String password;

    public @NotBlank(message = "email is reqiured") String getEmail() {
        return email;
    }

    public @NotBlank(message = "password is reqiured") String getPassword() {
        return password;
    }
}
