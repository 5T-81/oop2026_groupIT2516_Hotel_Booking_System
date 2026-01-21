package edu.aitu.oop3.services;

import edu.aitu.oop3.services.exceptions.PaymentDeclinedException;
import edu.aitu.oop3.services.interfaces.PaymentServiceInterface;

public class PaymentService implements PaymentServiceInterface {
    public boolean proccessPayment(String cardNumber, double amount) {
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
