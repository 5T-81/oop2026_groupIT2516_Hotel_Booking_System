package edu.aitu.oop3.milestone2.factory;

import edu.aitu.oop3.entities.Rooms;

public class Suite extends Rooms {
    public Suite(int room_id, String status) {
        super(room_id, status);
        this.type = RoomType.SUITE;
    }
}
