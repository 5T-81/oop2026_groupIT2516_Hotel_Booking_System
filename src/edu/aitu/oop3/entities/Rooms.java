package edu.aitu.oop3.entities;

import edu.aitu.oop3.milestone2.factory.RoomType;

public class Rooms {
    private int room_id;
    private double  room_number;
    private String status;
    protected RoomType type;

    public  Rooms(int room_id, String status){
        this.room_id = room_id;
        //this.room_number = room_number;
        this.status = status;;
    }
    public int getRoom_id() {return room_id;}
    public double getRoom_number() {return room_number;}
    public String getStatus() {return  status;}
    public RoomType getType() {return type;}

    @Override
    public String toString() {
        return "Rooms(room_id=" + room_id + ",room_number=" + room_number + ", status=" + status + "}";
    }
}
