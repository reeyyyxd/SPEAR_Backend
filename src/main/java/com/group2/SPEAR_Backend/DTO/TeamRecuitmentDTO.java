package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.TeamRecuitment.Status;

public class TeamRecuitmentDTO {

    private int trid;
    private int teamId;
    private int studentId;
    private String studentName;
    private String role;
    private String reason;
    private Status status;

    public TeamRecuitmentDTO() {}

    public TeamRecuitmentDTO(int teamId, int studentId, String studentName, String role, String reason, Status status) {
        this.teamId = teamId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.role = role;
        this.reason = reason;
        this.status = status;
    }


    public int getTrid() {
        return trid;
    }

    public void setTrid(int trid) {
        this.trid = trid;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
