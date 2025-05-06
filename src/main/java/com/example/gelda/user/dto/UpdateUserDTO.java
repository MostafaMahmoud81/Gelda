package com.example.gelda.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateUserDTO {


    @NotBlank(message = "Name is required")
    private String name;


    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Mobile number must be a valid Egyptian number")
    private String mobileNumber;

    // No-args constructor
    public UpdateUserDTO() {
    }

    // All-args constructor
    public UpdateUserDTO(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
