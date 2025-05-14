package com.example.gelda.transaction.dto;

import java.util.Date;

public class TransactionHistoryDTO {
    private String type;
    private Double amount;
    private String senderName;
    private String receiverName;
    private Date timestamp;

    public TransactionHistoryDTO(String type, Double amount, String senderName, String receiverName, Date timestamp) {
        this.type = type;
        this.amount = amount;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
