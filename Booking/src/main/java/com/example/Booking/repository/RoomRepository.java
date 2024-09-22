package com.example.Booking.repository;

import com.example.Booking.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();


    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.id NOT IN (SELECT b.room.id FROM Booking b WHERE b.checkinDate <= :checkOutDate AND b.checkoutDate >= :checkInDate)")
    List<Room> findAvailableRoomsByDatesAndTypes(@Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate, @Param("roomType") String roomType);


    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();
}
