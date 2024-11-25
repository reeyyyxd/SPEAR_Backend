package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "projectProposals")
public class ProjectProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public ProjectProposal() {
        super();
    }

    public ProjectProposal(User proposedBy, String projectName, Classes classProposal, String description, String status) {
        this.proposedBy = proposedBy;
        this.projectName = projectName;
        this.classProposal = classProposal;
        this.description = description;
        this.status = "PENDING";
    }

    public ProjectProposal(User proposedBy, String projectName, Classes classProposal, String description) {
        this.proposedBy = proposedBy;
        this.projectName = projectName;
        this.classProposal = classProposal;
        this.description = description;
        this.status = "PENDING";
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



}
