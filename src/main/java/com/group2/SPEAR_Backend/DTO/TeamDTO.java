package com.group2.SPEAR_Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class TeamDTO {

    private int tid;
    private String groupName;
    private String projectName;
    private Integer projectId;
    private String leaderName;
    private Long classId;
    private List<Integer> memberIds;
    private List<String> memberNames;
    private boolean isRecruitmentOpen;
    private List<FeatureDTO> features;
    private String projectDescription;
    private Integer adviserId;
    private Integer scheduleId;
    private String adviserName;  // New field for adviser's full name
    private String scheduleDay;  // New field for schedule day
    private String scheduleTime; // New field for schedule time


    public TeamDTO() {}

    public TeamDTO(int tid, String groupName, String projectName, Integer projectId, String leaderName, Long classId,
                   List<Integer> memberIds, boolean isRecruitmentOpen, List<FeatureDTO> features,
                   String projectDescription, Integer adviserId, Integer scheduleId) {
        this.tid = tid;
        this.groupName = groupName;
        this.projectName = (projectName != null) ? projectName : "No Project Assigned";
        this.projectId = projectId;
        this.leaderName = leaderName;
        this.classId = classId;
        this.memberIds = memberIds;
        this.isRecruitmentOpen = isRecruitmentOpen;
        this.features = features;
        this.projectDescription = (projectDescription != null) ? projectDescription : "No Description Available";
        this.adviserId = adviserId;
        this.scheduleId = scheduleId;
    }

    //getting teacher name and sched
    public TeamDTO(int tid, String groupName, String projectName, Integer projectId, String leaderName, Long classId,
                   List<Integer> memberIds, boolean isRecruitmentOpen, List<FeatureDTO> features,
                   String projectDescription, String adviserName, String scheduleDay, String scheduleTime,
                   List<String> memberNames) {
        this.tid = tid;
        this.groupName = groupName;
        this.projectName = (projectName != null) ? projectName : "No Project Assigned";
        this.projectId = projectId;
        this.leaderName = leaderName;
        this.classId = classId;
        this.memberIds = memberIds;
        this.memberNames = memberNames != null ? memberNames : new ArrayList<>();
        this.isRecruitmentOpen = isRecruitmentOpen;
        this.features = features;
        this.projectDescription = (projectDescription != null) ? projectDescription : "No Description Available";
        this.adviserName = (adviserName != null) ? adviserName : "No Adviser Assigned";
        this.scheduleDay = (scheduleDay != null) ? scheduleDay : "No Schedule Set";
        this.scheduleTime = (scheduleTime != null) ? scheduleTime : "No Schedule Set";
    }

    //fullname members
    public TeamDTO(int tid, String groupName, String projectName, Integer projectId, String leaderName, Long classId,
                   List<Integer> memberIds, boolean isRecruitmentOpen, List<FeatureDTO> features,
                   String projectDescription, Integer adviserId, Integer scheduleId, List<String> memberNames) {
        this.tid = tid;
        this.groupName = groupName;
        this.projectName = (projectName != null) ? projectName : "No Project Assigned";
        this.projectId = projectId;
        this.leaderName = leaderName;
        this.classId = classId;
        this.memberIds = memberIds;
        this.memberNames = memberNames; // Assigning member full names
        this.isRecruitmentOpen = isRecruitmentOpen;
        this.features = features;
        this.projectDescription = (projectDescription != null) ? projectDescription : "No Description Available";
        this.adviserId = adviserId;
        this.scheduleId = scheduleId;
    }


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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
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
        this.projectDescription = (projectDescription != null) ? projectDescription : "No Description Available";
    }

    public Integer getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(Integer adviserId) {
        this.adviserId = adviserId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(List<String> memberNames) {
        this.memberNames = memberNames;
    }

    public String getAdviserName() {
        return adviserName;
    }

    public void setAdviserName(String adviserName) {
        this.adviserName = adviserName;
    }

    public String getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(String scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}