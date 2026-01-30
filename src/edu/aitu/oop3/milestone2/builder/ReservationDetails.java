package edu.aitu.oop3.milestone2.builder;

import java.time.LocalDate;

public class ReservationDetails {
    private final int guest_id;
    private final int room_id;
    private final LocalDate check_in;
    private final LocalDate check_out;
    private final String paymentMethod;
    private final double amount;

    private ReservationDetails(Builder builder) {
        this.guest_id = builder.guest_id;
        this.room_id = builder.room_id;
        this.check_in = builder.check_in;
        this.check_out = builder.check_out;
        this.paymentMethod = builder.paymentMethod;
        this.amount = builder.amount;
    }

    //Builder starts here
    public static class Builder {
        private int guest_id;
        private int room_id;
        private LocalDate check_in;
        private LocalDate check_out;
        private String paymentMethod;
        private double amount;

        public Builder guest_id(int guest_id) {
            this.guest_id = guest_id;
            return this; //return the object itself not the value of ids
        }
        public Builder room_id(int room_id) {
            this.room_id = room_id;
            return this;
        }
        public Builder check_in(LocalDate check_in) {
            this.check_in = check_in;
            return this;
        }
        public Builder check_out(LocalDate check_out) {
            this.check_out = check_out;
            return this;
        }
        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }
        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }
        public ReservationDetails build() {
            // basic validation
            if (guest_id <= 0) throw new IllegalArgumentException("guestId must be > 0");
            if (room_id <= 0) throw new IllegalArgumentException("roomId must be > 0");
            if (check_in == null || check_out == null) throw new IllegalArgumentException("Dates are required");
            if (!check_in.isBefore(check_out)) throw new IllegalArgumentException("checkIn must be before checkOut");

            return new ReservationDetails(this);//creates the real object
        }
    }
}
