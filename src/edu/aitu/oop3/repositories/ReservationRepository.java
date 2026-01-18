package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Reservations;

import java.time.LocalDate;
import java.util.List;

//JOB:Talk to the database only.
public interface ReservationRepository {
    int create(Reservations reservation); //save something in the db
    Reservations findById(int id); //get one row by its ID
    List<Reservations> findAll(); // get all rows
    void cancel(int id); //delete a reservation
    boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut);
}
