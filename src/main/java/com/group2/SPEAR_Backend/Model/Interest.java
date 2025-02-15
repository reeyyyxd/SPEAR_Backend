package com.group2.SPEAR_Backend.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "interests")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iid")
    private int iid;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_interest", referencedColumnName = "uid")
    private User userInterest;

    private String interest;

    private String department;

    public Interest() {
        super();
    }
    public Interest( User userInterest, String interest, String department) {
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
