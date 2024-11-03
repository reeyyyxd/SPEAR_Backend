package com.group2.SPEAR_Backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "features")
public class FeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "pid")
    private ProjectProposalEntity project;

    @Column(name = "featureTitle")
    private String featureTitle;

    @Column(name = "featureDescription")
    private String featureDescription;

    public FeatureEntity() {
        super();
    }

    public FeatureEntity(int fid, String featureTitle, String featureDescription, ProjectProposalEntity project) {
        this.fid = fid;
        this.featureTitle = featureTitle;
        this.featureDescription = featureDescription;
        this.project = project;
    }

    public int getFid() {
        return fid;
    }

    public String getFeatureTitle() {
        return featureTitle;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public ProjectProposalEntity getProject() {
        return project;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setFeatureTitle(String featureTitle) {
        this.featureTitle = featureTitle;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }

    public void setProject(ProjectProposalEntity project) {
        this.project = project;
    }
}
