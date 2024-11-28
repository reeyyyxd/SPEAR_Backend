package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.TeamRecuitment;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Service.TeamRecuitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TeamRecuitmentController {

    @Autowired
    private TeamRecuitmentService trServ;

    @PostMapping("/student/apply")
    public ResponseEntity<String> applyToTeam(
            @RequestParam int teamId,
            @RequestBody User student,
            @RequestParam String role,
            @RequestParam String reason) {
        trServ.applyToTeam(teamId, student, role, reason);
        return ResponseEntity.ok("Application submitted successfully.");
    }

    @GetMapping("/team/{teamId}/pending")
    public List<TeamRecuitment> getPendingApplications(@PathVariable int teamId) {
        return trServ.getPendingApplicationsByTeam(teamId);
    }

    @PostMapping("/student/review/{recruitmentId}")
    public ResponseEntity<String> reviewApplication(
            @PathVariable int recruitmentId,
            @RequestParam boolean isAccepted,
            @RequestParam(required = false) String leaderReason) {
        trServ.reviewApplication(recruitmentId, isAccepted, leaderReason);
        String message = isAccepted ? "Application accepted" : "Application rejected";
        return ResponseEntity.ok(message);
    }
}
