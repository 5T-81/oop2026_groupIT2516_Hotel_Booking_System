package edu.aitu.oop3.services;

import edu.aitu.oop3.repositories.ReservationRepository;
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

public void createReservation(int room_id, LocalDate check_in, LocalDate check_out) {
    if(!reservationRepository.isRoomAvailable(room_id,check_in,check_out)) {//need new implelment
        throw new RoomNotAvailableException("Room" + room_id + "is not available from" + check_in +     "to" + check_out);
        }
    }
 }
