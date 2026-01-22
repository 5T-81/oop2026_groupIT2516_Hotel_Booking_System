package edu.aitu.oop3.services;

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
    public boolean pay(String cardNumber, double amount) {//(need update) use payment repo to insert the data created
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new PaymentDeclinedException("Card number is invalid");
        }
        if (amount <= 0) {
            throw new PaymentDeclinedException("Payment amount cannot be negative");
        }

        if (amount > 1000) {
    throw new PaymentDeclinedException("Payment declined: amount exceeds limit");
        }
        return true;
    }
}
