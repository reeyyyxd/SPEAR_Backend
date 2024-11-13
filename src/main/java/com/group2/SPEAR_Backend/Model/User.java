package com.group2.SPEAR_Backend.Model;
import jakarta.persistence.*;
import java.util.Set;


@Entity
@Table(name="users")

public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int uid;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "role")
    private String role;


    public User() {
        super();
    }

    public User(String firstname, String lastname, String email,
                String password, String role) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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


}