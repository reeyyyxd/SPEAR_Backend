package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Service.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173", "http://10.147.17.166:5173"})
public class ProjectProposalController {

    @Autowired
    private ProjectProposalService ppServ;

    @PostMapping("/student/create-proposal")
    public ResponseEntity<Map<String, String>> createProposal(@RequestBody Map<String, Object> payload) {
        try {
            ProjectProposalDTO dto = new ProjectProposalDTO(
                    0,
                    (String) payload.get("projectName"),
                    (String) payload.get("description"),
                    "PENDING",
                    null,
                    (int) payload.get("proposedById"),
                    Long.valueOf(String.valueOf(payload.get("classId"))),
                    payload.get("teamId") != null ? (Integer) payload.get("teamId") : null,
                    false
            );

            List<Map<String, String>> featurePayload = (List<Map<String, String>>) payload.get("features");
            List<FeatureDTO> features = featurePayload != null
                    ? featurePayload.stream().map(f -> new FeatureDTO(f.get("featureTitle"), f.get("featureDescription"))).toList()
                    : null;

            ppServ.createProjectProposal(dto, features);
            return ResponseEntity.ok(Map.of("message", "Proposal created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/proposals/class/{classId}/student/{studentId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByClassAndStudent(@PathVariable Long classId, @PathVariable int studentId) {
        return ResponseEntity.ok(ppServ.getProposalsByClassAndStudent(classId, studentId));
    }

    @GetMapping("/proposals/adviser/{adviserId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByAdviser(@PathVariable int adviserId) {
        return ResponseEntity.ok(ppServ.getProposalsByAdviser(adviserId));
    }

    @GetMapping("/proposals/status/{status}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ppServ.getProposalsByStatus(status));
    }

    @PutMapping("/student/update-proposal/{proposalId}")
    public ResponseEntity<Map<String, String>> updateProposal(@PathVariable int proposalId, @RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, String>> featurePayload = (List<Map<String, String>>) payload.get("features");
            List<FeatureDTO> features = featurePayload != null
                    ? featurePayload.stream().map(f -> new FeatureDTO(f.get("featureTitle"), f.get("featureDescription"))).toList()
                    : null;

            ppServ.updateProposalAndFeatures(proposalId, (String) payload.get("projectName"), (String) payload.get("description"), features);
            return ResponseEntity.ok(Map.of("message", "Proposal updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/student/delete-proposal/{id}")
    public ResponseEntity<Map<String, String>> deleteProposal(@PathVariable int id) {
        return ResponseEntity.ok(Map.of("message", ppServ.deleteProjectProposal(id)));
    }

    @PutMapping("/teacher/status-proposal/{id}")
    public ResponseEntity<Map<String, String>> updateProposalStatus(@PathVariable int id, @RequestBody Map<String, String> payload) {
        try {
            ppServ.updateProposalStatus(id, payload.get("status"), payload.get("reason"));
            return ResponseEntity.ok(Map.of("message", "Proposal status updated to " + payload.get("status")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/student/update-denied/{proposalId}")
    public ResponseEntity<Map<String, String>> updateDeniedToPending(@PathVariable int proposalId) {
        ppServ.updateDeniedToPending(proposalId);
        return ResponseEntity.ok(Map.of("message", "Proposal updated to PENDING status"));
    }

    @PutMapping("/student/set-to-open-project/{proposalId}")
    public ResponseEntity<Map<String, String>> updateApprovedToOpenProject(@PathVariable int proposalId) {
        ppServ.updateApprovedToOpenProject(proposalId);
        return ResponseEntity.ok(Map.of("message", "Proposal updated to OPEN PROJECT status"));
    }


}