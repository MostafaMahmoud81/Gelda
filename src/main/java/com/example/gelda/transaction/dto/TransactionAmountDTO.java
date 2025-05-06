package com.example.gelda.transaction.dto;

public class TransactionAmountDTO {

    private double amount;

    // Constructors, getters, and setters
    public TransactionAmountDTO() {}

    public TransactionAmountDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
