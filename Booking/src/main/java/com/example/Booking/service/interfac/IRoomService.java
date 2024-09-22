package com.example.Booking.service.interfac;

import com.example.Booking.dto.Response;
import com.example.Booking.models.Room;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface IRoomService {
    Response addNewRoom(String photo, String roomType, BigDecimal roomPrice, String description);

    List<String> getAllRoomTypes();

    Response getAllRoom();
    Response deleteRoom(Long roomId);
    Response updateRoom(Long id,String photo, String roomType, BigDecimal roomPrice, String description);
    Response getRoomById(Long roomId);
    Response getAvailableRoomsByDateandType(LocalDate checkinDate, LocalDate checkoutDate, String roomType);
    Response getAllAvailableRomm();
}
