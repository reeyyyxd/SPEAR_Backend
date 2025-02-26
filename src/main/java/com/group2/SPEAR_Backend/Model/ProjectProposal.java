package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "projectProposals")
public class ProjectProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    //change the proposed by to
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "proposed_by", referencedColumnName = "uid")
    private User proposedBy;

    @Column(name = "projectName")
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "class_proposal", referencedColumnName = "cid")
    private Classes classProposal;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "adviser", referencedColumnName = "uid", nullable = true)
    private User adviser;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    //do something, no constructors yet.
    private String ratings;

    public ProjectProposal() {
    }

    public ProjectProposal(User proposedBy, String projectName, Classes classProposal, String description, User adviser) {
        this.proposedBy = proposedBy;
        this.projectName = projectName;
        this.classProposal = classProposal;
        this.description = description;
        this.status = "PENDING";
        this.adviser = adviser;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public User getProposedBy() {
        return proposedBy;
    }

    public void setProposedBy(User proposedBy) {
        this.proposedBy = proposedBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Classes getClassProposal() {
        return classProposal;
    }

    public void setClassProposal(Classes classProposal) {
        this.classProposal = classProposal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getAdviser() {
        return adviser;
    }

    public void setAdviser(User adviser) {
        this.adviser = adviser;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }
}
