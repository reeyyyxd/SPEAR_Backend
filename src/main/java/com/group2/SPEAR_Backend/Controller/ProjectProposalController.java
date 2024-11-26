package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Service.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/student/create")
    public ResponseEntity<Map<String, String>> createProposal(@RequestBody ProjectProposalDTO dto) {
        ppServ.createProjectProposal(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Proposal created");
        return ResponseEntity.ok(response);
    }

    @PutMapping("student/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteProposal(@PathVariable int id) {
        String message = ppServ.deleteProjectProposal(id);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping("/proposals/class/with-features/{classId}")
    public ResponseEntity<List<ProjectProposalDTO>> getProposalsByClassWithFeatures(@PathVariable Long classId) {
        List<ProjectProposalDTO> proposals = ppServ.getProposalsByClassWithFeatures(classId);
        return ResponseEntity.ok(proposals);
    }

    @PutMapping("/teacher/status/{id}")
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

}
