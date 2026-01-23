package edu.aitu.oop3.services.interfaces;

import edu.aitu.oop3.entities.Reservations;

import java.time.LocalDate;
import java.util.List;

public interface ReservationServiceInterface {
    //for creating
    int createReservation(int guest_id, int room_id, LocalDate check_in, LocalDate check_out);

    //for cancelling
    void cancelReservation(int reservation_id);

    //Reservations getReservationById(int reservation_id);
    //List<Reservations> getAllReservations();
}
