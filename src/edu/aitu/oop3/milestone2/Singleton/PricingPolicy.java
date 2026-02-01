package edu.aitu.oop3.milestone2.Singleton;

import edu.aitu.oop3.milestone2.factory.RoomType;

public class PricingPolicy {
    // Private constructor
    private PricingPolicy() {
    }

    //  Static inner holder class; hold only the one obj created
    private static class PricingPolicyHolder {
        private static final PricingPolicy INSTANCE = new PricingPolicy();
    }

    //  Public access point to reach the obj (instance)
    public static PricingPolicy getInstance() {
        return PricingPolicyHolder.INSTANCE;
    }

    //  Business pricing
    public double calculatePrice(RoomType type) {
        return switch (type) {
            case STANDARD -> 100.0;
            case SUITE -> 200.0;
            case STUDIO -> 150.0;
            default -> throw new IllegalArgumentException("Unsupported room type");
        };
    }
}
