package com.group2.SPEAR_Backend.Entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    // Many-to-one relationship with ProjectProposalEntity
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "pid")
    private ProjectProposalEntity project;

    @Column(name = "groupno")
    private int groupNo;

    // Many-to-one relationship with UserEntity as team leader
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "uid")
    private UserEntity leader;

    @Column(name = "memberid")
    private int memberId;

    // Many-to-many relationship with users
    @ManyToMany(mappedBy = "teams")
    private Set<UserEntity> users;

    public TeamEntity() {
        super();
    }

    public TeamEntity(int tid, ProjectProposalEntity project, int groupNo, UserEntity leader, int memberId) {
        this.tid = tid;
        this.project = project;
        this.groupNo = groupNo;
        this.leader = leader;
        this.memberId = memberId;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public ProjectProposalEntity getProject() {
        return project;
    }

    public void setProject(ProjectProposalEntity project) {
        this.project = project;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public UserEntity getLeader() {
        return leader;
    }

    public void setLeader(UserEntity leader) {
        this.leader = leader;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
