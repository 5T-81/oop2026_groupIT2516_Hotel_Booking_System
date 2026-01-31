package edu.aitu.oop3.milestone2.lambda;

import edu.aitu.oop3.entities.Rooms;

import java.util.List;
import java.util.function.Predicate;

public class RoomSearchService {

    public List<Rooms> filterRooms(List<Rooms> rooms, Predicate<Rooms> condition) {
        return rooms.stream()
                .filter(room -> room.getStatus().equals("available"))   // <-- lambda is USED here
                .toList();
    }
}
