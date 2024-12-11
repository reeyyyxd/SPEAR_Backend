package com.group2.SPEAR_Backend.DTO;

import java.time.LocalDate;

public class EvaluationDTO {

    private Long eid;
    private String availability;
    private LocalDate dateOpen;
    private LocalDate dateClose;
    private String period;
    private Long classId;
    private String courseCode;
    private String section;
    private String courseDescription;

    public EvaluationDTO() {}

    public EvaluationDTO(Long eid, String availability, LocalDate dateOpen, LocalDate dateClose, String period,
                         Long classId, String courseCode, String section, String courseDescription) {
        this.eid = eid;
        this.availability = availability;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.period = period;
        this.classId = classId;
        this.courseCode = courseCode;
        this.section = section;
        this.courseDescription = courseDescription;
    }

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
