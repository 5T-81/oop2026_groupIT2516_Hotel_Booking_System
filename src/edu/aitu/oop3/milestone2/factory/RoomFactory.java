package edu.aitu.oop3.milestone2.factory;

import edu.aitu.oop3.entities.Rooms;

public class RoomFactory {
    public static Rooms createRoom(RoomType type, int room_id, String status) {
        switch (type) {
            case STANDARD:
                return new StandardRoom(room_id,status);
            case SUITE:
                return new Suite(room_id, status);
            case STUDIO:
                return new Studio(room_id, status);
            default:
                throw new IllegalArgumentException("Unknown room type");
        }
    }
}
