package com.group2.SPEAR_Backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    @Column(name = "groupno")
    private int groupNo;

    @Column(name = "memberid")
    private int memberId;

    public TeamEntity() {
        super();
    }

    public TeamEntity(int tid, int groupNo, int memberId) {
        this.tid = tid;
        this.groupNo = groupNo;
        this.memberId = memberId;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
