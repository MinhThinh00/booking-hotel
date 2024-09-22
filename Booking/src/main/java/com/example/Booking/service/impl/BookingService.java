package com.example.Booking.service.impl;

import com.example.Booking.BookingApplication;
import com.example.Booking.dto.BookingDTO;
import com.example.Booking.dto.Response;
import com.example.Booking.exception.OurException;
import com.example.Booking.models.Booking;
import com.example.Booking.models.Room;
import com.example.Booking.models.User;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.RoomRepository;
import com.example.Booking.repository.UserRepository;
import com.example.Booking.service.interfac.IBookingService;
import com.example.Booking.service.interfac.IRoomService;
import com.example.Booking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{
            if (bookingRequest.getCheckoutDate().isBefore(bookingRequest.getCheckinDate())) {
                throw new IllegalArgumentException("Check in date must come after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(()->new OurException("Room not found"));

            List<Booking> existingBookings= room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Room not Available for selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        }catch (OurException e ){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Saving a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try{
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Booking mot found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBooking() {
        Response response = new Response();
        try{
            List<Booking> bookingList= bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
            bookingRepository.findById(bookingId).orElseThrow(()->new OurException("Booking doess not exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successfull");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return  response;
    }
    boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings){
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckinDate().equals(existingBooking.getCheckinDate())
                                || bookingRequest.getCheckoutDate().isBefore(existingBooking.getCheckoutDate())
                                || (bookingRequest.getCheckinDate().isAfter(existingBooking.getCheckinDate())
                                && bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckoutDate()))
                                || (bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckinDate())

                                && bookingRequest.getCheckoutDate().equals(existingBooking.getCheckoutDate()))
                                || (bookingRequest.getCheckinDate().isBefore(existingBooking.getCheckinDate())

                                && bookingRequest.getCheckoutDate().isAfter(existingBooking.getCheckoutDate()))

                                || (bookingRequest.getCheckinDate().equals(existingBooking.getCheckoutDate())
                                && bookingRequest.getCheckoutDate().equals(existingBooking.getCheckinDate()))

                                || (bookingRequest.getCheckinDate().equals(existingBooking.getCheckoutDate())
                                && bookingRequest.getCheckoutDate().equals(bookingRequest.getCheckinDate()))
                );
    }
}
