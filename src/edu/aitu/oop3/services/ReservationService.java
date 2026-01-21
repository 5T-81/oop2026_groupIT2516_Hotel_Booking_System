package edu.aitu.oop3.services;

import edu.aitu.oop3.repositories.ReservationRepository;
import edu.aitu.oop3.services.exceptions.RoomNotAvailableException;
import edu.aitu.oop3.services.interfaces.ReservationServiceInterface;

import java.time.LocalDate;

public class ReservationService implements ReservationServiceInterface {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
}
public void createReservation(int room_id, LocalDate check_in, LocalDate check_out) {
    if(!reservationRepository.isRoomAvailable(room_id,check_in,check_out)) {
        throw new RoomNotAvailableException("Room" + room_id + "is not available from" + check_in +     "to" + check_out);
        }
    }
 }
