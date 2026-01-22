package edu.aitu.oop3.services;

import edu.aitu.oop3.entities.Reservations;
import edu.aitu.oop3.repositories.ReservationRepository;
import edu.aitu.oop3.services.exceptions.InvalidDateRangeException;
import edu.aitu.oop3.services.exceptions.RoomNotAvailableException;
import edu.aitu.oop3.services.interfaces.ReservationServiceInterface;

import java.time.LocalDate;

public class ReservationService implements ReservationServiceInterface {
    private final ReservationRepository reservationRepository;
    private final PaymentService paymentService;
    private final RoomAvailabilityService availabilityService;
    //constructor
    public ReservationService(ReservationRepository reservationRepository, PaymentService paymentService, RoomAvailabilityService availabilityService) {
        this.reservationRepository = reservationRepository;
        this.paymentService = paymentService;
        this.availabilityService = availabilityService;
    }

    @Override
    public int createReservation(int room_id, LocalDate check_in, LocalDate check_out) {
        //check date validation
        if(check_in == null || check_out == null || check_in.isBefore(check_out)) {
            throw new InvalidDateRangeException("Invalid date range");
        }
        //check if avaliable
        boolean avaliable = availabilityService.isRoomAvailable(room_id, check_in, check_out);
        if(!avaliable) {
            throw new RoomNotAvailableException("Room " + room_id+ "is not available");
        }
        //create reservation entity
        Reservations reservation = new Reservations(0, 0, room_id,check_in,check_out);
        //returned the id of the entity created
        int reservationId = reservationRepository.create(reservation);
        //payment
        return reservationId;
    }
    @Override
    public void cancelReservation(int reservation_Id) {
        if(reservation_Id <= 0)
        {
            throw new IllegalArgumentException("Invalid reservation Id");
        }
        //check if reservation exist
        Reservations existing = reservationRepository.findById(reservation_Id);
        if(existing == null) {
            throw new RuntimeException("Reservation id not found");
        }
        //actual cancelling
        reservationRepository.cancel(reservation_Id);
    }



 }

