package edu.aitu.oop3.entities;

public class Guests {
    private int guest_id;
    private String first_name;
    private String last_name;

    public Guests(int id, String first_name,String last_name) {
        this.guest_id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    //getters and setters
    public int getId() {return guest_id;}
    public String getFirst_name() {return first_name;}
    public String getLast_name() {return last_name;}

    @Override
    public String toString(){
        return "Guests{id=" + guest_id + ",first_name=" + first_name + ",last_name=" + last_name + "}";
    }
}
