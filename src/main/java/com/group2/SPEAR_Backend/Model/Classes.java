//package com.group2.SPEAR_Backend.Model;
//
//import jakarta.persistence.*;
//
//import java.util.Set;
//
//@Entity
//@Table(name = "classes")
//public class Classes {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int cid;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "created_by", referencedColumnName = "uid")
//    private User createdBy;
//
//    @Column(name = "className")
//    private String className;
//
//    @Column(name = "classCode")
//    private String classCode;
//
//    @Column(name = "section")
//    private String section;
//
//    @Column(name = "schoolYear")
//    private String schoolYear;
//
//    @Column(name = "semester")
//    private String semester;
//
//    @Column(name = "classDescription")
//    private String classDescription;
//
//    @ManyToMany(mappedBy = "enrolledClasses")
//    private Set<User> students;
//
//    public Classes() {
//        super();
//    }
//
//    public Classes(int cid, String className, String classCode, String section, String schoolYear,
//                   String semester, String classDescription) {
//        this.cid = cid;
//        this.className = className;
//        this.classCode = classCode;
//        this.section = section;
//        this.schoolYear = schoolYear;
//        this.semester = semester;
//        this.classDescription = classDescription;
//    }
//
//    public int getCid() {
//        return cid;
//    }
//
//    public void setCid(int cid) {
//        this.cid = cid;
//    }
//
//    public String getClassName() {
//        return className;
//    }
//
//    public void setClassName(String name) {
//        this.className = name;
//    }
//
//    public String getClassCode() {
//        return classCode;
//    }
//
//    public void setClassCode(String classCode) {
//        this.classCode = classCode;
//    }
//
//    public String getSection() {
//        return section;
//    }
//
//    public void setSection(String section) {
//        this.section = section;
//    }
//
//    public String getSchoolYear() {
//        return schoolYear;
//    }
//
//    public void setSchoolYear(String schoolYear) {
//        this.schoolYear = schoolYear;
//    }
//
//    public String getSemester() {
//        return semester;
//    }
//
//    public void setSemester(String semester) {  // Corrected parameter name
//        this.semester = semester;
//    }
//
//    public String getClassDescription() {
//        return classDescription;
//    }
//
//    public void setClassDescription(String classDescription) {
//        this.classDescription = classDescription;
//    }
//
//    public Set<User> getStudents() {
//        return students;
//    }
//
//    public void setStudents(Set<User> students) {
//        this.students = students;
//    }
//}
