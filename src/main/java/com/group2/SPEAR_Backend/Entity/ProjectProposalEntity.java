package com.group2.SPEAR_Backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "projectProposals")
public class ProjectProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    // Relationship with the User entity
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proposed_by", referencedColumnName = "uid")
    private UserEntity proposedBy;

    @Column(name = "projectName")
    private String projectName;

    // Relationship with the Classes entity
    @ManyToOne
    @JoinColumn(name = "class_proposal", referencedColumnName = "cid")
    private ClassesEntity classProposal;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;

    public ProjectProposalEntity() {
        super();
    }

    public ProjectProposalEntity(int pid, UserEntity proposedBy, String projectName, ClassesEntity classProposal, String description, String status, String reason) {
        this.pid = pid;
        this.proposedBy = proposedBy;
        this.projectName = projectName;
        this.classProposal = classProposal;
        this.description = description;
        this.status = status;
        this.reason = reason;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public UserEntity getProposedBy() {
        return proposedBy;
    }

    public void setProposedBy(UserEntity proposedBy) {
        this.proposedBy = proposedBy;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ClassesEntity getClassProposal() {
        return classProposal;
    }

    public void setClassProposal(ClassesEntity classProposal) {
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
