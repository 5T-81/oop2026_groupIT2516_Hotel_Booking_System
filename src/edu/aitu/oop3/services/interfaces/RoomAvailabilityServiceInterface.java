package edu.aitu.oop3.services.interfaces;

import java.time.LocalDate;

public interface RoomAvailabilityServiceInterface {
    boolean isRoomAvailable(int room_id, LocalDate check_in, LocalDate check_out);
}
