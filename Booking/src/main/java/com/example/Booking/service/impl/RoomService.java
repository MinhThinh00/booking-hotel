package com.example.Booking.service.impl;

import com.example.Booking.dto.Response;
import com.example.Booking.dto.RoomDTO;
import com.example.Booking.exception.OurException;
import com.example.Booking.models.Room;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.RoomRepository;
import com.example.Booking.service.interfac.IRoomService;
import com.example.Booking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Service

public class RoomService implements IRoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Override
    public Response addNewRoom(String photo, String roomType, BigDecimal roomPrice, String description) {
        Response response= new Response();
        try{
            Room room= new Room();
            room.setRoomPhotoUrl(photo);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom= roomRepository.save(room);
            RoomDTO roomDTO= new RoomDTO();
            roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);
        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRoom() {
        Response response= new Response();
        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> roomListDTO = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomListDTO);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response= new Response();
        try{
            roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("successfull");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId,String photo, String roomType, BigDecimal roomPrice, String description) {
        Response response= new Response();
        try{
            Room room =  roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));

            if(photo!=null) room.setRoomPhotoUrl(photo);
            if(roomType!=null)room.setRoomType(roomType);
            if(roomPrice!=null)room.setRoomPrice(roomPrice);
            if(description!=null)room.setRoomDescription(description);


            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setRoom(roomDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response= new Response();
        try{
            Room room= roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateandType(LocalDate checkinDate, LocalDate checkoutDate, String roomType) {
        Response response= new Response();
        try{
            List<Room> availableRoom= roomRepository.findAvailableRoomsByDatesAndTypes(checkinDate, checkoutDate,roomType);
            List<RoomDTO> roomDTOList= Utils.mapRoomListEntityToRoomListDTO(availableRoom);

            response.setStatusCode(200);
            response.setMessage("successfull");
            response.setRoomList(roomDTOList);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRomm() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a room " + e.getMessage());
        }
        return response;
    }
}
