package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ScheduleDTO;
import com.group2.SPEAR_Backend.DTO.StatusDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.Team;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173", "http://10.147.17.166:5173"})
public class TeamController {

    @Autowired
    private TeamService tServ;

//    @PostMapping("/student/create/{projectId}")
//    public Team createTeam(@PathVariable int projectId, @RequestParam String groupName) {
//        return tServ.createTeam(projectId, groupName);
//    }


    @PostMapping("/student/create-team")
    public ResponseEntity<Map<String, String>> createTeam(@RequestBody Map<String, String> requestBody) {
        Map<String, String> response = new HashMap<>();
        try {
            int leaderId = Integer.parseInt(requestBody.getOrDefault("leaderId", "0"));
            Long classId = Long.parseLong(requestBody.getOrDefault("classId", "0"));
            String groupName = requestBody.get("groupName");

            if (leaderId == 0 || classId == 0 || groupName == null || groupName.trim().isEmpty()) {
                response.put("error", "Missing or invalid input data.");
                return ResponseEntity.badRequest().body(response);
            }

            // Create team with only leader and class, adviser & schedule will be null
            tServ.createTeam(leaderId, classId, groupName);
            response.put("message", "Team has been created successfully with recruitment OPEN.");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("error", "Invalid number format for leaderId or classId.");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "An error occurred while creating the team: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PutMapping("/student/{teamId}/update-group-name")
    public ResponseEntity<Map<String, String>> updateGroupName(
            @PathVariable int teamId,
            @RequestBody Map<String, String> requestBody
    ) {
        String groupName = requestBody.get("groupName");
        int requesterId = Integer.parseInt(requestBody.get("requesterId")); // Extract requester ID

        tServ.updateGroupName(teamId, groupName, requesterId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Group name has been updated");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("student/delete-team/{teamId}/requester/{requesterId}")
    public ResponseEntity<Map<String, String>> deleteTeam(
            @PathVariable int teamId,
            @PathVariable int requesterId
    ) {
        tServ.deleteTeam(teamId, requesterId);
        return ResponseEntity.ok(Map.of("message", "Team has been deleted."));
    }

    @PutMapping("/student/{teamId}/open-recruitment")
    public ResponseEntity<StatusDTO> openRecruitment(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody
    ) {
        int requesterId = requestBody.get("requesterId"); // Extract requester ID
        return ResponseEntity.ok(tServ.openRecruitment(teamId, requesterId));
    }

    @PutMapping("/student/{teamId}/close-recruitment")
    public ResponseEntity<StatusDTO> closeRecruitment(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody
    ) {
        int requesterId = requestBody.get("requesterId"); // Extract requester ID
        return ResponseEntity.ok(tServ.closeRecruitment(teamId, requesterId));
    }


    @DeleteMapping("/student/{teamId}/kick-member/{memberId}")
    public ResponseEntity<Map<String, String>> kickMember(
            @PathVariable int teamId,
            @PathVariable int memberId,
            @RequestBody Map<String, Integer> requestBody
    ) {
        int requesterId = requestBody.get("requesterId"); // Extract requester ID
        tServ.kickMember(teamId, requesterId, memberId);

        return ResponseEntity.ok(Map.of("message", "A member has been removed from the team."));
    }

    @PutMapping("/team/{teamId}/transfer-leadership")
    public ResponseEntity<Map<String, String>> transferLeadership(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody) {
        int requesterId = requestBody.get("requesterId"); // Extract requester ID
        int newLeaderId = requestBody.get("newLeaderId"); // Extract new leader ID
        tServ.transferLeadership(teamId, requesterId, newLeaderId);

        return ResponseEntity.ok(Map.of("message", "Leadership transferred successfully."));
    }

    @PostMapping("/team/{teamId}/add-member")
    public ResponseEntity<Map<String, String>> addPreferredMember(
            @PathVariable int teamId,
            @RequestBody Map<String, Integer> requestBody) {
        int requesterId = requestBody.get("requesterId"); // Extract requester ID
        int memberId = requestBody.get("memberId"); // Extract new member ID
        tServ.addPreferredMember(teamId, requesterId, memberId);

        return ResponseEntity.ok(Map.of("message", "Member added successfully."));
    }

    @GetMapping("/teams/all-active")
    public List<TeamDTO> getAllActiveTeams() {
        return tServ.getAllActiveTeams();
    }

    @GetMapping("/teams/{teamId}")
    public TeamDTO getTeamById(@PathVariable int teamId) {
        return tServ.getTeamById(teamId);
    }


    @GetMapping("/teams/class/{classId}")
    public ResponseEntity<?> getTeamsByClass(@PathVariable int classId) {
        try {
            List<TeamDTO> teams = tServ.getTeamsByClassId(classId);
            return ResponseEntity.ok(teams);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/team/my/{classId}/{userId}")
    public ResponseEntity<?> getMyTeam(@PathVariable int userId, @PathVariable int classId) {
        try {
            TeamDTO myTeam = tServ.getMyTeam(userId, classId);
            return ResponseEntity.ok(myTeam);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No team found.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<List<UserDTO>> getTeamMembers(@PathVariable int teamId) {
        List<UserDTO> members = tServ.getTeamMembers(teamId);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/team/{teamId}/leave")
    public ResponseEntity<Map<String, String>> leaveTeam(
            @PathVariable int teamId,
            @RequestParam int userId) {

        tServ.leaveTeam(teamId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "You have successfully left the team.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/team/{classId}/available-students")
    public ResponseEntity<List<UserDTO>> getAvailableStudents(
            @PathVariable Long classId,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {
        List<UserDTO> students = tServ.getAvailableStudentsForTeam(classId, searchTerm);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/class/{classId}/qualified-teachers")
    public ResponseEntity<List<UserDTO>> getQualifiedAdvisersForClass(@PathVariable Long classId) {
        List<UserDTO> advisers = tServ.getQualifiedAdvisersForClass(classId);
        return ResponseEntity.ok(advisers);
    }
    //schedule to change
    @GetMapping("/adviser/{adviserId}/available-schedules")
    public ResponseEntity<List<ScheduleDTO>> getAvailableSchedulesForAdviser(@PathVariable int adviserId) {
        List<ScheduleDTO> schedules = tServ.getAvailableSchedulesForAdviser(adviserId);
        return ResponseEntity.ok(schedules);
    }











}
