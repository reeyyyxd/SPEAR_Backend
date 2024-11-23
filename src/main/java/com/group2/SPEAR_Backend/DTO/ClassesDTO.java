package com.group2.SPEAR_Backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.User;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassesDTO {

    private int statusCode;
    private String error;
    private String message;

    private Long cid;
    private Long uid;
    private String courseType;
    private String courseCode;
    private String section;
    private String schoolYear;
    private String semester;
    private String courseDescription;

    private Classes classes;
    private List<Classes> classesList;
    private User createdBy;
    private boolean isDeleted;
    private String courseName;
    private String classKey;
    private List<User> enrolledStudents;

    private String firstname;
    private String lastname;
    private String role;

    public ClassesDTO() {
        super();
    }

    public ClassesDTO(String courseType, String courseCode, String section, String schoolYear, String semester, String courseDescription, String courseName, String classKey, User createdBy) {
        this.courseType = courseType;
        this.courseCode = courseCode;
        this.section = section;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.courseDescription = courseDescription;
        this.courseName = courseName;
        this.classKey = classKey;
        this.createdBy = createdBy;
    }

    public ClassesDTO(int statusCode, String message, List<Classes> classesList) {
        this.statusCode = statusCode;
        this.message = message;
        this.classesList = classesList;
    }

    public ClassesDTO(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }

    // class data with user details
    public ClassesDTO(String classKey, String courseCode, String courseDescription, String courseName,
                      String courseType, String schoolYear, String section, String semester,
                      String firstname, String lastname, String role) {
        this.classKey = classKey;
        this.courseCode = courseCode;
        this.courseDescription = courseDescription;
        this.courseName = courseName;
        this.courseType = courseType;
        this.schoolYear = schoolYear;
        this.section = section;
        this.semester = semester;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    //classes that students are enrolled in
    public ClassesDTO(String courseCode, String courseDescription, String courseName,
                      String courseType, String schoolYear, String section, String semester, User createdBy) {
        this.courseCode = courseCode;
        this.courseDescription = courseDescription;
        this.courseName = courseName;
        this.courseType = courseType;
        this.schoolYear = schoolYear;
        this.section = section;
        this.semester = semester;
        this.firstname = createdBy.getFirstname();
        this.lastname = createdBy.getLastname();
        this.role = createdBy.getRole();
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public List<Classes> getClassesList() {
        return classesList;
    }

    public void setClassesList(List<Classes> classesList) {
        this.classesList = classesList;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassKey() {
        return classKey;
    }

    public void setClassKey(String classKey) {
        this.classKey = classKey;
    }

    public List<User> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<User> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
