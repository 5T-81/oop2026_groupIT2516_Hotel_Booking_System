package edu.aitu.oop3.milestone2.factory;

import edu.aitu.oop3.entities.Rooms;

public class Studio extends Rooms {
    public Studio(int room_id, String status) {
        super(room_id, status);
        this.type = RoomType.STUDIO;
    }
}
