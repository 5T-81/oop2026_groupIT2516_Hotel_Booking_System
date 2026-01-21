package edu.aitu.oop3.services;

import edu.aitu.oop3.repositories.ReservationRepository;
import edu.aitu.oop3.services.exceptions.InvalidDateRangeException;
import edu.aitu.oop3.services.interfaces.RoomAvailabilityServiceInterface;

import java.time.LocalDate;

public class RoomAvailabilityService implements RoomAvailabilityServiceInterface{
    //field
    private final ReservationRepository reservationRepository;

    //constructor
    public RoomAvailabilityService(ReservationRepository res) {
        this.reservationRepository = res;
    }

    @Override
    public boolean isRoomAvailable(int room_id, LocalDate check_in, LocalDate check_out){

        //check if
        if (check_in == null || check_out == null || !check_in.isBefore(check_out)) {
            throw new InvalidDateRangeException("Invalid date range");
        }
        //return T or F if room available
        return reservationRepository.isRoomAvailable(room_id, check_in, check_out);//this form our repo class
    }
}
