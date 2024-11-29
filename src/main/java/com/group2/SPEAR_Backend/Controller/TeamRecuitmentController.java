package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.TeamRecuitmentDTO;
import com.group2.SPEAR_Backend.Model.TeamRecuitment;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Service.TeamRecuitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TeamRecuitmentController {

    @Autowired
    private TeamRecuitmentService trServ;

    @PostMapping("/student/apply")
    public ResponseEntity<String> applyToTeam(@RequestBody Map<String, Object> requestBody) {
        int teamId = (int) requestBody.get("teamId");
        int studentId = (int) requestBody.get("uid");
        String role = (String) requestBody.get("role");
        String reason = (String) requestBody.get("reason");

        User student = new User();
        student.setUid(studentId);

        trServ.applyToTeam(teamId, student, role, reason);

        return ResponseEntity.ok("Application submitted successfully.");
    }


    @GetMapping("/team/{teamId}/pending")
    public List<TeamRecuitmentDTO> getPendingApplications(@PathVariable int teamId) {
        return trServ.getPendingApplicationsByTeam(teamId);
    }

    @PostMapping("/student/review/{recruitmentId}")
    public ResponseEntity<String> reviewApplication(
            @PathVariable int recruitmentId,
            @RequestBody Map<String, Object> requestBody) {
        boolean isAccepted = (boolean) requestBody.get("isAccepted");
        String leaderReason = (String) requestBody.getOrDefault("leaderReason", null);

        trServ.reviewApplication(recruitmentId, isAccepted, leaderReason);
        String message = isAccepted ? "Application accepted" : "Application rejected";
        return ResponseEntity.ok(message);
    }

}
