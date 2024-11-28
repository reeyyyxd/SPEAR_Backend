package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TeamController {

    @Autowired
    private TeamService tServ;

//    @PostMapping("/student/create/{projectId}")
//    public Team createTeam(@PathVariable int projectId, @RequestParam String groupName) {
//        return tServ.createTeam(projectId, groupName);
//    }


    @PostMapping("/student/create-team/{projectId}")
    public ResponseEntity<Map<String, String>> createTeam(
            @PathVariable int projectId,
            @RequestBody Map<String, String> requestBody
    ) {
        String groupName = requestBody.get("groupName");
        tServ.createTeam(projectId, groupName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Team has been created");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/student/{teamId}/update-group-name")
    public ResponseEntity<Map<String, String>> updateGroupName(
            @PathVariable int teamId,
            @RequestBody Map<String, String> requestBody
    ) {
        String groupName = requestBody.get("groupName");
        tServ.updateGroupName(teamId, groupName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Group name has been updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("student/delete-team/{teamId}")
    public String deleteTeam(@PathVariable int teamId) {
        return tServ.deleteTeam(teamId);
    }

    @PutMapping("/student/{teamId}/open-recruitment")
    public StatusDTO openRecruitment(@PathVariable int teamId) {
        return tServ.openRecruitment(teamId);
    }

    @PutMapping("/student/{teamId}/close-recruitment")
    public StatusDTO closeRecruitment(@PathVariable int teamId) {
        return tServ.closeRecruitment(teamId);
    }


    @DeleteMapping("/student/{teamId}/kick-member/{memberId}")
    public Team kickMember(@PathVariable int teamId, @PathVariable int memberId) {
        return tServ.kickMember(teamId, memberId);
    }

    @GetMapping("/teams/all-active")
    public List<TeamDTO> getAllActiveTeams() {
        return tServ.getAllActiveTeams();
    }

    @GetMapping("/teams/{teamId}")
    public TeamDTO getTeamById(@PathVariable int teamId) {
        return tServ.getTeamById(teamId);
    }
}
