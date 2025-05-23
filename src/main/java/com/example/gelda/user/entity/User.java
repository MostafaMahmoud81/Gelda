package com.example.gelda.user.entity;

import com.example.gelda.wallet.entity.Wallet; // Import Wallet entity


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String nationalId;
    private String mobileNumber;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet; // One-to-one relationship with Wallet entity


    // Constructors
    public User() {
    }

    public User(String name, String email, String password, String nationalId, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nationalId = nationalId;
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
