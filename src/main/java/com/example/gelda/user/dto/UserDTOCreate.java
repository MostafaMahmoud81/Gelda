package com.example.gelda.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTOCreate {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^[0-9]{14}$", message = "National ID must be 14 digits")
    private String nationalId;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^01[0125][0-9]{8}$", message = "Mobile number must be a valid Egyptian number")
    private String mobileNumber;

    // Constructors
    public UserDTOCreate() {}

    public UserDTOCreate(String name, String email, String password, String nationalId, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nationalId = nationalId;
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
