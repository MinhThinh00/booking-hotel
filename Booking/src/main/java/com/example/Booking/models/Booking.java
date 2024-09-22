    package com.example.Booking.models;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Future;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotNull;
    import lombok.Data;
    import lombok.NonNull;

    import java.time.LocalDate;

    @Data
    @Entity
    @Table(name="bookings")
    public class Booking {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull (message="checkin date is required")
        private LocalDate checkinDate;

        @Future(message = "checkout date must be in the future")
        private LocalDate checkoutDate;

        @Min(value = 1, message = "Number of adult must not be less than 1 ")
        private int numOfAdult;

        @Min(value = 0, message = "Number of adult must not be less than 0 ")
        private int numOfChildren;

        private int totalNumofGuest;

        private String bookingConfirmationCode;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name= "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="room_id")
        private Room room;

        public void calculateTotalNumOfGuest(){
            this.totalNumofGuest= this.numOfAdult+ this.numOfChildren;
        }

        public void setNumOfAdult( int numOfAdult) {
            this.numOfAdult = numOfAdult;
            calculateTotalNumOfGuest();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public @NotNull(message = "checkin date is required") LocalDate getCheckinDate() {
            return checkinDate;
        }

        public void setCheckinDate(@NotNull(message = "checkin date is required") LocalDate checkinDate) {
            this.checkinDate = checkinDate;
        }

        public @Future(message = "checkout date must be in the future") LocalDate getCheckoutDate() {
            return checkoutDate;
        }

        public void setCheckoutDate(@Future(message = "checkout date must be in the future") LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
        }

        @Min(value = 1, message = "Number of adult must not be less than 1 ")
        public int getNumOfAdult() {
            return numOfAdult;
        }

        @Min(value = 0, message = "Number of adult must not be less than 0 ")
        public int getNumOfChildren() {
            return numOfChildren;
        }

        public int getTotalNumofGuest() {
            return totalNumofGuest;
        }

        public void setTotalNumofGuest(int totalNumofGuest) {
            this.totalNumofGuest = totalNumofGuest;
        }

        public String getBookingConfirmationCode() {
            return bookingConfirmationCode;
        }

        public void setBookingConfirmationCode(String bookingConfirmationCode) {
            this.bookingConfirmationCode = bookingConfirmationCode;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Room getRoom() {
            return room;
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public void setNumOfChildren(int numOfChildren) {
            this.numOfChildren = numOfChildren;
            calculateTotalNumOfGuest();
        }

        @Override
        public String toString() {
            return "Booking{" +
                    "id=" + id +
                    ", checkinDate=" + checkinDate +
                    ", checkoutDate=" + checkoutDate +
                    ", numOfAdult=" + numOfAdult +
                    ", numOfChildren=" + numOfChildren +
                    ", totalNumofGuest=" + totalNumofGuest +
                    ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                    '}';
        }

        public int getNumOfAdults() {
            return this.numOfAdult= numOfAdult;
        }

        public int getTotalNumOfGuest() {
            return this.totalNumofGuest= totalNumofGuest;
        }
    }
