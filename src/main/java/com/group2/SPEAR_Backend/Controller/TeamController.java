package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService tServ;

    @PostMapping("/student/create/{projectId}")
    public Team createTeam(@PathVariable int projectId, @RequestParam String groupName) {
        return tServ.createTeam(projectId, groupName);
    }

    @PutMapping("/student/{teamId}/update-group-name")
    public Team updateGroupName(@PathVariable int teamId, @RequestParam String groupName) {
        return tServ.updateGroupName(teamId, groupName);
    }

    @DeleteMapping("student/delete-team/{teamId}")
    public String deleteTeam(@PathVariable int teamId) {
        return tServ.deleteTeam(teamId);
    }

    @PutMapping("/student/{teamId}/open-recruitment")
    public Team openRecruitment(@PathVariable int teamId) {
        return tServ.openRecruitment(teamId);
    }

    @PutMapping("/student/{teamId}/close-recruitment")
    public Team closeRecruitment(@PathVariable int teamId) {
        return tServ.closeRecruitment(teamId);
    }

    @DeleteMapping("/student/{teamId}/kick-member/{memberId}")
    public Team kickMember(@PathVariable int teamId, @PathVariable int memberId) {
        return tServ.kickMember(teamId, memberId);
    }

    @GetMapping("/teams/all-active")
    public List<Team> getAllActiveTeams() {
        return tServ.getAllActiveTeams();
    }

    @GetMapping("/teams/{teamId}")
    public Team getTeamById(@PathVariable int teamId) {
        return tServ.getTeamById(teamId);
    }
}
