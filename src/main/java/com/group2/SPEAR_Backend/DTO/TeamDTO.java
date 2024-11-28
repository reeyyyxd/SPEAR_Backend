package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.User;

import java.util.List;

public class TeamDTO {

    private int tid;
    private String groupName;
    private int projectId;
    private int leaderId;
    private int classId;
    private List<Integer> memberIds;
    private boolean isRecruitmentOpen;

    public TeamDTO() {
    }

    public TeamDTO(int tid, String groupName, int projectId, int leaderId, int classId, List<Integer> memberIds, boolean isRecruitmentOpen) {
        this.tid = tid;
        this.groupName = groupName;

        this.projectId = projectId;
        this.leaderId = leaderId;
        this.classId = classId;
        this.memberIds = memberIds;
        this.isRecruitmentOpen = isRecruitmentOpen;
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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
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
}
