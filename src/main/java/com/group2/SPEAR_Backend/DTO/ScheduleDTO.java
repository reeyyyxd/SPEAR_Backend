package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.Schedule;

public class ScheduleDTO {

    private int schedid;
    private String day;
    private String time;
    private int teacherId;
    private String teacherName;
    private Long classId;
    private String className;
    private String courseDescription;

    public ScheduleDTO() {}

    public ScheduleDTO(int schedid, String day, String time, int teacherId, String teacherName, Long classId, String className, String courseDescription) {
        this.schedid = schedid;
        this.day = day;
        this.time = time;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.classId = classId;
        this.className = className;
        this.courseDescription = courseDescription;
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getSchedid(),
                schedule.getDay(),
                schedule.getTime(),
                schedule.getTeacher().getUid(),
                schedule.getTeacher().getFirstname() + " " + schedule.getTeacher().getLastname(),
                schedule.getScheduleOfClasses().getCid(),
                schedule.getScheduleOfClasses().getCourseCode(),
                schedule.getScheduleOfClasses().getCourseDescription()
        );
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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}