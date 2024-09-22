package com.example.Booking.service.impl;

import com.example.Booking.dto.LoginRequest;
import com.example.Booking.dto.Response;
import com.example.Booking.dto.UserDTO;
import com.example.Booking.exception.OurException;
import com.example.Booking.models.User;
import com.example.Booking.repository.UserRepository;
import com.example.Booking.service.interfac.IUserService;
import com.example.Booking.utils.JWTUtils;
import com.example.Booking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response= new Response();
        try{
            if(user.getRole()==null || user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser= userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (OurException e){
                response.setStatusCode(400);
                response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Registration " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response= new Response();
        try{
 //           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user= userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("User not found"));
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String a=passwordEncoder.encode(loginRequest.getPassword());
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new OurException("Invalid credentials" + "--"+ a+" --"+user);
            }

            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");
            response.setMessage("successful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Login " + e.getMessage()+"--"+loginRequest.getPassword());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response= new Response();
        try {
            List<User> user= userRepository.findAll();
            List<UserDTO> userDTOList= Utils.mapUserListEntityToUserListDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response= new Response();

        try{
            User user= userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("user not found"));
            UserDTO userDTO= Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
           // System.out.println("User found: " + user.toString());
            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setUser(userDTO);
           // System.out.println("UserDTO created: " + userDTO.toString());
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
            //e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response= new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("user not found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response= new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfor(String email) {
        Response response= new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }
}
