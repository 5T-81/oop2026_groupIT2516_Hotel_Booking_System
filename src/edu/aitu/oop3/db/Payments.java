package edu.aitu.oop3.db;


public class Payments {
    private int payment_id;
    private int reservation_id;
    private double amount;

    public Payments(int payment_id, int reservation_id, double amount){
        this.payment_id = payment_id;
        this.reservation_id = reservation_id;
        this.amount = amount;
    }
    public int getPayment_id() {return payment_id;}
    public int getReservation_id() {return reservation_id;}
    public double getAmount() {return amount;}

    @Override
    public String toString() {
        return "Payments{payment_id=" + payment_id + ", reservation_id=" + reservation_id + ", amount=" + amount + "}";

    }

}
