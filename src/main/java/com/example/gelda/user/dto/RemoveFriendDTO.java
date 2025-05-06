package com.example.gelda.user.dto;

public class RemoveFriendDTO {
    private String friendMobileNumber;

    public RemoveFriendDTO() {}

    public RemoveFriendDTO(String friendMobileNumber) {
        this.friendMobileNumber = friendMobileNumber;
    }

    public String getFriendMobileNumber() {
        return friendMobileNumber;
    }

    public void setFriendMobileNumber(String friendMobileNumber) {
        this.friendMobileNumber = friendMobileNumber;
    }
}
