package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.User;

public class InterestDTO {
    private int iid;
    private User userInterest;
    private String interest;
    private String department;

    public InterestDTO() {
        super();
    }

    public InterestDTO(int iid, User userInterest, String interest, String department) {
        this.iid = iid;
        this.userInterest = userInterest;
        this.interest = interest;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
