package com.example.gelda.user.dto;

public class AddFriendDTO {
    private String friendMobileNumber;

    public AddFriendDTO() {}

    public AddFriendDTO(String friendMobileNumber) {
        this.friendMobileNumber = friendMobileNumber;
    }

    public String getFriendMobileNumber() {
        return friendMobileNumber;
    }

    public void setFriendMobileNumber(String friendMobileNumber) {
        this.friendMobileNumber = friendMobileNumber;
    }
}
