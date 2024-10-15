package com.group2.SPEAR_Backend.Entity;
import jakarta.persistence.*;


@Entity
@Table(name="users")

public class UserEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int uid;

    @Column(name="idnumber")
    private String idNumber;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "interest")
    private String interest;

    @Column(name = "role")
    private String role;



    public UserEntity() {
        super();
    }

    public UserEntity(int uid, String idNumber, String firstname, String lastname, String email,
                      String password, String interest, String role) {
        super();
        this.uid = uid;
        this.idNumber = idNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.interest=interest;
        this.role = role;

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String name) {
        this.idNumber = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInterest() {
        return interest;
    }
    public void setInterest(String Interest) {
        this.interest = interest;
    }


}