package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Service.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectProposalController {

    @Autowired
    private ProjectProposalService ppServ;

    @PostMapping("/student/create-proposal")
    public ResponseEntity<Map<String, String>> createProposalWithFeatures(
            @RequestBody Map<String, Object> payload) {

        try {
            int proposedById = (int) payload.get("proposedById");
            String projectName = (String) payload.get("projectName");
            String description = (String) payload.get("description");
            Long classId = Long.valueOf((int) payload.get("classId"));
            Integer adviserId = payload.get("adviserId") != null ? (int) payload.get("adviserId") : null;

            ProjectProposalDTO dto = new ProjectProposalDTO(
                    0,
                    projectName,
                    description,
                    "PENDING",
                    null,
                    proposedById,
                    classId,
                    adviserId,
                    false
            );

            List<Map<String, String>> featurePayload = (List<Map<String, String>>) payload.get("features");
            List<FeatureDTO> features = featurePayload != null
                    ? featurePayload.stream()
                    .map(f -> new FeatureDTO(f.get("featureTitle"), f.get("featureDescription")))
                    .toList()
                    : null;

            ppServ.createProjectProposal(dto, features);

            return ResponseEntity.ok(Map.of("message", "Proposal with features created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/student/update-proposal/{proposalId}")
    public ResponseEntity<Map<String, String>> updateProposalAndFeatures(
            @PathVariable int proposalId,
            @RequestBody Map<String, Object> payload) {

        try {
            String projectName = (String) payload.get("projectName");
            String description = (String) payload.get("description");
            List<Map<String, String>> featurePayload = (List<Map<String, String>>) payload.get("features");

            List<FeatureDTO> features = featurePayload.stream()
                    .map(f -> new FeatureDTO(f.get("featureTitle"), f.get("featureDescription")))
                    .toList();

            ppServ.updateProposalAndFeatures(proposalId, projectName, description, features);

            return ResponseEntity.ok(Map.of("message", "Proposal and features updated successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/student/update-adviser/{proposalId}")
    public ResponseEntity<Map<String, String>> updateCapstoneAdviser(
            @PathVariable int proposalId,
            @RequestParam int adviserId) {

        try {
            ppServ.updateCapstoneAdviser(proposalId, adviserId);
            return ResponseEntity.ok(Map.of("message", "Adviser updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("student/delete-proposal/{id}")
    public ResponseEntity<Map<String, String>> deleteProposal(@PathVariable int id) {
        String message = ppServ.deleteProjectProposal(id);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping("/proposals/class/with-features/{classId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByClassWithFeatures(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassWithFeatures(classId);
        return ResponseEntity.ok(proposals);
    }

    @PutMapping("/teacher/status-proposal/{id}")
    public ResponseEntity<Map<String, String>> updateProposalStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> payload) {
        try {
            String status = payload.get("status");
            String reason = payload.get("reason");
            ppServ.updateProposalStatus(id, status, reason);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Proposal status updated to " + status);
            if ("DENIED".equalsIgnoreCase(status)) {
                response.put("reason", reason);
            }
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/proposals/class/{classId}/student/{studentId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByClassAndStudent(
            @PathVariable Long classId, @PathVariable int studentId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassAndStudent(classId, studentId);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/adviser/{adviserId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByAdviser(
            @PathVariable int adviserId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByAdviser(adviserId);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/status/{status}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByStatus(
            @PathVariable String status) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByStatus(status);
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/class/{classId}/approved")
    public ResponseEntity<List<ProjectProposalDTO>> getApprovedProposalsByClass(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassAndStatus(classId, "APPROVED");
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/class/{classId}/denied")
    public ResponseEntity<List<ProjectProposalDTO>> getDeniedProposalsByClass(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassAndStatus(classId, "DENIED");
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/class/{classId}/pending")
    public ResponseEntity<List<ProjectProposalDTO>> getPendingProposalsByClass(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassAndStatus(classId, "PENDING");
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/class/{classId}/open-projects")
    public ResponseEntity<List<ProjectProposalDTO>> getOpenProjectsByClass(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassAndStatus(classId, "OPEN PROJECT");
        return ResponseEntity.ok(proposals);
    }

    @GetMapping("/proposals/abandoned")
    public ResponseEntity<List<ProjectProposalDTO>> getAbandonedProposals() {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByStatus("OPEN PROJECT");
        return ResponseEntity.ok(proposals);
    }
    @PutMapping("student/update-denied/{proposalId}")
    public ResponseEntity<Map<String, String>> updateDeniedToPending(@PathVariable int proposalId) {
        ppServ.updateDeniedToPending(proposalId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Proposal updated to PENDING status");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/student/set-to-open-project/{proposalId}")
    public ResponseEntity<Map<String, String>> updateApprovedToOpenProject(@PathVariable int proposalId) {
        ppServ.updateApprovedToOpenProject(proposalId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Proposal updated to OPEN PROJECT status");
        return ResponseEntity.ok(response);
    }

    @PostMapping("student/get-project/{proposalId}")
    public ResponseEntity<Map<String, String>> assignStudentToOpenProject(
            @PathVariable int proposalId,
            @RequestParam int studentId) {
        ppServ.assignStudentToOpenProject(proposalId, studentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student assigned to the project successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proposals/{proposalId}/adviser")
    public ResponseEntity<Map<String, String>> getAdviserNameByProposalId(@PathVariable int proposalId) {
        try {
            String adviserFullName = ppServ.getAdviserFullNameByProposalId(proposalId);
            if (adviserFullName == null) {
                return ResponseEntity.ok(Map.of("adviserFullName", "N/A"));
            }
            return ResponseEntity.ok(Map.of("adviserFullName", adviserFullName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/teams/leader/{leaderId}")
    public ResponseEntity<Map<String, String>> getLeaderNameById(@PathVariable int leaderId) {
        try {
            String leaderName = ppServ.getLeaderNameById(leaderId);
            if (leaderName == null) {
                return ResponseEntity.ok(Map.of("leaderName", "N/A"));
            }
            return ResponseEntity.ok(Map.of("leaderName", leaderName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }







}
