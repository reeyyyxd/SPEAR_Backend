package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.User;

public class InterestDTO {
    private int iid;
    private User userInterest;
    private String interest;

    public InterestDTO() {
        super();
    }

    public InterestDTO(int iid, User userInterest, String interest) {
        this.iid = iid;
        this.userInterest = userInterest;
        this.interest = interest;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public User getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(User userInterest) {
        this.userInterest = userInterest;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
