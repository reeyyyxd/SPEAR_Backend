package com.group2.SPEAR_Backend.DTO;

import java.util.List;

public class ProjectProposalDTO {

    private int pid;
    private String projectName;
    private String description;
    private String status;
    private String reason;
    private int proposedById;
    private Long classId;
    private Integer adviserId;
    private Boolean isDeleted;
    private List<FeatureDTO> features;

    public ProjectProposalDTO() {}

    //non-capstone
    public ProjectProposalDTO(int pid, String projectName, String description, String status, String reason,
                              int proposedById, Long classId, Boolean isDeleted) {
        this.pid = pid;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.reason = reason;
        this.proposedById = proposedById;
        this.classId = classId;
        this.isDeleted = isDeleted;
    }

    //capstone
    public ProjectProposalDTO(int pid, String projectName, String description, String status, String reason,
                              int proposedById, Long classId, Integer adviserId, Boolean isDeleted) {
        this.pid = pid;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.reason = reason;
        this.proposedById = proposedById;
        this.classId = classId;
        this.adviserId = adviserId;
        this.isDeleted = isDeleted;
    }
    //with features
    public ProjectProposalDTO(int pid, String projectName, String description, String status, String reason,
                              int proposedById, Long classId, Integer adviserId, Boolean isDeleted, List<FeatureDTO> features) {
        this.pid = pid;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.reason = reason;
        this.proposedById = proposedById;
        this.classId = classId;
        this.adviserId = adviserId;
        this.isDeleted = isDeleted;
        this.features = features;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public int getProposedById() {
        return proposedById;
    }

    public void setProposedById(int proposedById) {
        this.proposedById = proposedById;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(Integer adviserId) {
        this.adviserId = adviserId;
    }

    public List<FeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDTO> features) {
        this.features = features;
    }
}
