package com.group2.SPEAR_Backend.DTO;

import java.util.List;

public class TeamProjectDTO {
    private int teamId;
    private String teamName;
    private int projectId;
    private String projectName;
    private String projectDescription;
    private String projectStatus;
    private String projectRating;
    private List<String> teamMembers;

    public TeamProjectDTO(int teamId, String teamName, int projectId, String projectName,
                          String projectDescription, String projectStatus, String projectRating,
                          List<String> teamMembers) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStatus = projectStatus;
        this.projectRating = projectRating;
        this.teamMembers = teamMembers;
    }

    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public int getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getProjectDescription() { return projectDescription; }
    public String getProjectStatus() { return projectStatus; }
    public String getProjectRating() { return projectRating; }
    public List<String> getTeamMembers() { return teamMembers; }
}