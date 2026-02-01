package edu.aitu.oop3.milestone2.lambda;

import edu.aitu.oop3.entities.Rooms;

import java.util.List;
import java.util.function.Predicate;

public class RoomSearchService {

    public List<Rooms> filterRooms(List<Rooms> rooms, Predicate<Rooms> isFree) {
        List<Rooms> result = rooms.stream() //convert the list to stream, so can filter, map, sort, etc.
                .filter(isFree) //filter method of stream (Keep only the elements for which condition returns true)
                .toList();
        return result;
    }
}
//predicate: functional interface returns T or F