package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schedid;

    @Column(name = "day", nullable = false)
    private String day;

    @Column(name = "time", nullable = false)
    private String time;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "uid", nullable = false)
    private User teacher;

    public Schedule() {}

    public Schedule(String day, String time, User teacher) {
        this.day = day;
        this.time = time;
        this.teacher = teacher;
    }

    public int getSchedid() {
        return schedid;
    }
    public void setSchedid(int schedid) {
        this.schedid = schedid;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public User getTeacher() {
        return teacher;
    }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
