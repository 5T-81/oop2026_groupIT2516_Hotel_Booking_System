package edu.aitu.oop3.milestone2.Singleton;

import edu.aitu.oop3.milestone2.factory.RoomType;

public class PricingPolicy {
    // Private constructor
    private PricingPolicy() {
    }

    //  Static inner holder class
    private static class PricingPolicyHolder {
        private static final PricingPolicy INSTANCE = new PricingPolicy();
    }

    //  Public access point
    public static PricingPolicy getInstance() {
        return PricingPolicyHolder.INSTANCE;
    }

    //  Business logic methods (example)
    public double calculatePrice(RoomType type) {
        return switch (type) {
            case STANDARD -> 100.0;
            case SUITE -> 200.0;
            case DORM -> 50.0;
            default -> throw new IllegalArgumentException("Unsupported room type");
        };
    }

    //In services or Main:LATER
   // PricingPolicy pricing = PricingPolicy.getInstance();
    //double price = pricing.calculatePrice(RoomType.SUITE);
}
