package com.example.gelda.transaction.dto;

public class TransferTransactionDTO {

    private String receiverMobileNumber;
    private double amount;

    // Constructors, getters, and setters
    public TransferTransactionDTO() {}

    public TransferTransactionDTO(String receiverMobileNumber, double amount) {
        this.receiverMobileNumber = receiverMobileNumber;
        this.amount = amount;
    }

    public String getReceiverMobileNumber() {
        return receiverMobileNumber;
    }

    public void setReceiverMobileNumber(String receiverMobileNumber) {
        this.receiverMobileNumber = receiverMobileNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
