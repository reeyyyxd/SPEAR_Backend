package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "pid")
    private ProjectProposal project;

    @ManyToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "uid")
    private User leader;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "cid")
    private Classes classRef;

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    @Column(name = "is_recruitment_open")
    private boolean isRecruitmentOpen;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public Team() {}

    public Team(ProjectProposal project, User leader, Classes classRef, String groupName) {
        this.project = project;
        this.leader = leader;
        this.classRef = classRef;
        this.groupName = groupName;
        this.isRecruitmentOpen = true;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public ProjectProposal getProject() {
        return project;
    }

    public void setProject(ProjectProposal project) {
        this.project = project;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public Classes getClassRef() {
        return classRef;
    }

    public void setClassRef(Classes classRef) {
        this.classRef = classRef;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public boolean isRecruitmentOpen() {
        return isRecruitmentOpen;
    }

    public void setRecruitmentOpen(boolean recruitmentOpen) {
        isRecruitmentOpen = recruitmentOpen;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
