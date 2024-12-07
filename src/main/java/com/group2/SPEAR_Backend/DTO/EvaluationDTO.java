package com.group2.SPEAR_Backend.DTO;

import java.time.LocalDate;

public class EvaluationDTO {

    private Long eid;
//    private String status;
    private String availability;
    private LocalDate dateOpen;
    private LocalDate dateClose;
    private String period;
    private Long classId;

    public EvaluationDTO() {}

    public EvaluationDTO(Long eid, String availability, LocalDate dateOpen, LocalDate dateClose, String period, Long classId) {
        this.eid = eid;
        this.availability = availability;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.period = period;
        this.classId = classId;
    }

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public LocalDate getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(LocalDate dateOpen) {
        this.dateOpen = dateOpen;
    }

    public LocalDate getDateClose() {
        return dateClose;
    }

    public void setDateClose(LocalDate dateClose) {
        this.dateClose = dateClose;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

}
