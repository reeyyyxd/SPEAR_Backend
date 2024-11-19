package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "classes")
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "createdBy", referencedColumnName = "uid")
    private User createdBy;


    @Column(name = "course_type", nullable = false)
    private String courseType; // Capstone or Non-Capstone

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "section", nullable = false)
    private String section;

    @Column(name = "school_year", nullable = false)
    private String schoolYear;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

//    @Column(name = "created_date", nullable = false)
//    private LocalDate createdDate;

    private Boolean isDeleted = false;

    public Boolean getIsDeleted() {
        return isDeleted != null ? isDeleted : false; // Default to false if null
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public Classes(){
        super();
    }


    public Classes(User createdBy, String courseType, String courseCode, String section, String schoolYear, String semester, String courseDescription, boolean isDeleted) {
        this.createdBy = createdBy;
        this.courseType = courseType;
        this.courseCode = courseCode;
        this.section = section;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.courseDescription = courseDescription;
        this.isDeleted = isDeleted;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
