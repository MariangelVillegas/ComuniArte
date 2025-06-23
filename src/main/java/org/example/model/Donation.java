package org.example.model;

public class Donation {
    private String userId;
    private double amount;

    public Donation() {}

    public Donation(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }
}
