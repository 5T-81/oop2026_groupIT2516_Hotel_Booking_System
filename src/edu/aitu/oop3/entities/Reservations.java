package edu.aitu.oop3.entities;

import java.time.LocalDate;

public class Reservations {
    private int reservation_id;
    private int guest_id;
    private int room_id;
    private LocalDate check_in;
    private LocalDate check_out;
public Reservations(int reservation_id, int guest_id, int room_id, LocalDate check_in,LocalDate check_out){
    this.reservation_id = reservation_id;
    this.guest_id = guest_id;
    this.room_id = room_id;
    this.check_in = check_in;
    this.check_out = check_out;
}
public int getReservation_id() {return reservation_id;}
    public int getGuest_id() {return guest_id;}
    public int getRoom_id() {return room_id;}
    public LocalDate getCheck_in() {return check_in;}
    public LocalDate getCheck_out() {return check_out;}

    @Override
     public String toString() {
    return "Reservations{reservation_id=" + reservation_id + ", guest_id=" + guest_id + ", room_id=" + room_id + ", check_in=" + check_in + ", check_out=" + check_out + "}";

    }
}
