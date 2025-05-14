package com.example.gelda.user.dto;

public class UserIdAndNameDTO {
    private Long id;
    private String name;

    public UserIdAndNameDTO(Long id, String fullName) {
        this.id = id;
        this.name = fullName;
    }

    // Getters and setters
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
}
