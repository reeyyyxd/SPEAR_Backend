package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
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
    public ResponseEntity<Map<String, String>> kickMember(@PathVariable int teamId, @PathVariable int memberId) {
        tServ.kickMember(teamId, memberId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "A member has been removed from the team.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teams/all-active")
    public List<TeamDTO> getAllActiveTeams() {
        return tServ.getAllActiveTeams();
    }

    @GetMapping("/teams/{teamId}")
    public TeamDTO getTeamById(@PathVariable int teamId) {
        return tServ.getTeamById(teamId);
    }

    @PutMapping("/team/{teamId}/transfer-leadership")
    public ResponseEntity<Map<String, String>> transferLeadership(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody) {
        int newLeaderId = requestBody.get("newLeaderId");
        tServ.transferLeadership(teamId, newLeaderId);
        return ResponseEntity.ok(Map.of("message", "Leadership transferred successfully."));
    }

    @PostMapping("/team/{teamId}/add-member")
    public ResponseEntity<Map<String, String>> addPreferredMember(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody) {
        int memberId = requestBody.get("memberId");
        tServ.addPreferredMember(teamId, memberId);

        return ResponseEntity.ok(Map.of("message", "Member added successfully."));
    }

    @GetMapping("/teams/class/{classId}")
    public ResponseEntity<List<TeamDTO>> getTeamsByClass(@PathVariable int classId) {
        List<TeamDTO> teams = tServ.getTeamsByClassId(classId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/team/my/{classId}")
    public ResponseEntity<TeamDTO> getMyTeam(
            @RequestParam int userId,
            @PathVariable int classId) {
        TeamDTO myTeam = tServ.getMyTeam(userId, classId);
        return ResponseEntity.ok(myTeam);
    }

    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<List<UserDTO>> getTeamMembers(@PathVariable int teamId) {
        List<UserDTO> members = tServ.getTeamMembers(teamId);
        return ResponseEntity.ok(members);
    }










}
