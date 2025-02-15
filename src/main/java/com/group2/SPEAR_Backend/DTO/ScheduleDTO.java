package com.group2.SPEAR_Backend.DTO;

public class ScheduleDTO {

    private int schedid;
    private String day;
    private String time;
    private int teacherId;
    private String teacherName;

    public ScheduleDTO() {}

    public ScheduleDTO(int schedid, String day, String time, int teacherId, String teacherName) {
        this.schedid = schedid;
        this.day = day;
        this.time = time;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
