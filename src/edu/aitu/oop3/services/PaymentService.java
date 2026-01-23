package edu.aitu.oop3.services;

import edu.aitu.oop3.entities.Payments;
import edu.aitu.oop3.repositories.ReservationRepository;
import edu.aitu.oop3.repositories.PaymentRepository;
import edu.aitu.oop3.services.exceptions.PaymentDeclinedException;
import edu.aitu.oop3.services.interfaces.PaymentServiceInterface;

public class PaymentService implements PaymentServiceInterface {

    //fields
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    //constructor
    public PaymentService(PaymentRepository paymetnRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymetnRepository;
        this.reservationRepository = reservationRepository;
    }

    //methods
    @Override //check if pay should return boolean or no need.(if not update it to void here and in interface too)
    public int pay(int reservation_id,double amount, String payment_method) {//(need update) use payment repo to insert the data created
        //if reservation doesn't exist in the first place
        if(reservationRepository.findById(reservation_id) == null){
            throw new PaymentDeclinedException("Reservation Not Found" + reservation_id);
        }
        if (amount <= 0) {
            throw new PaymentDeclinedException("Payment amount must be greater than 0");
        }

        if (amount > 1000) {
    throw new PaymentDeclinedException("Payment declined: amount exceeds limit");
        }
        //create payment in repo
        int paymentId = paymentRepository.createPayment(reservation_id, amount,payment_method);
        return paymentId;
    }
}
