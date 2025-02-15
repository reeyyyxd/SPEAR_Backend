package com.group2.SPEAR_Backend.DTO;

import java.util.List;

public class TeamDTO {

    private int tid;
    private String groupName;
    private String projectName;
    private int projectId;
    private int leaderId;
    private Long classId;
    private List<Integer> memberIds;
    private boolean isRecruitmentOpen;
    private List<FeatureDTO> features;
    private String projectDescription;
    private int adviserId;
    private int scheduleId;

    public TeamDTO() {}

    public TeamDTO(int tid, String groupName, String projectName, int projectId, int leaderId, Long classId,
                   List<Integer> memberIds, boolean isRecruitmentOpen, List<FeatureDTO> features,
                   String projectDescription, int adviserId, int scheduleId) {
        this.tid = tid;
        this.groupName = groupName;
        this.projectName = projectName;
        this.projectId = projectId;
        this.leaderId = leaderId;
        this.classId = classId;
        this.memberIds = memberIds;
        this.isRecruitmentOpen = isRecruitmentOpen;
        this.features = features;
        this.projectDescription = projectDescription;
        this.adviserId = adviserId;
        this.scheduleId = scheduleId;
    }

    // Getters and Setters
    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public boolean isRecruitmentOpen() {
        return isRecruitmentOpen;
    }

    public void setRecruitmentOpen(boolean recruitmentOpen) {
        isRecruitmentOpen = recruitmentOpen;
    }

    public List<FeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDTO> features) {
        this.features = features;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(int adviserId) {
        this.adviserId = adviserId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
