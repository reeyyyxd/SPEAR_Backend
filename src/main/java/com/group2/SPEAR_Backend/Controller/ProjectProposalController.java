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

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createProposal(@RequestBody ProjectProposalDTO dto) {
        ppServ.createProjectProposal(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Proposal created");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ProjectProposal>> getAllProposals() {
        return ResponseEntity.ok(ppServ.getAllProjectProposals());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectProposal> updateProposalStatus(
            @PathVariable int id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        ProjectProposal updatedProposal = ppServ.updateProposalStatus(id, status, reason);
        return ResponseEntity.ok(updatedProposal);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProposal(@PathVariable int id) {
        return ResponseEntity.ok(ppServ.deleteProjectProposal(id));
    }
}
