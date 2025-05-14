package com.example.gelda.user.dto;

public class UserWalletInfoDTO {
    private String name;
    private Long walletId;
    private Double balance;

    public UserWalletInfoDTO(String fullName, Long walletId, Double balance) {
        this.name = fullName;
        this.walletId = walletId;
        this.balance = balance;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
