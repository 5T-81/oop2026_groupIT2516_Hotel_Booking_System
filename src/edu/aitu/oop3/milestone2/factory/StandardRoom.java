package edu.aitu.oop3.milestone2.factory;

import edu.aitu.oop3.entities.Rooms;

public class StandardRoom extends Rooms {
    StandardRoom(int room_id, String status) {
        super(room_id, status);
        this.type = RoomType.STANDARD;
    }
}
