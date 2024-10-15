package com.group2.SPEAR_Backend.Entity;
import jakarta.persistence.*;

@Entity
@Table(name="classes")

public class ClassesEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cid;

    @Column(name="className")
    private String className;

    @Column(name="classCode")
    private String classCode;

    @Column(name="section")
    private String section;

    @Column(name="schoolYear")
    private String schoolYear;

    @Column(name="semester")
    private String semester;

    @Column(name = "classDescription")
    private String classDescription;



    public ClassesEntity() {
        super();
    }

    public ClassesEntity(int cid, String className, String classCode, String section, String schoolYear,
                         String semester, String classDescription) {
        super();
        this.cid = cid;
        this.className = className;
        this.classCode = classCode;
        this.section = section;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.classDescription = classDescription;

    }
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String name) {
        this.className = name;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
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

    public void setSemester(String password) {
        this.semester = password;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }



}