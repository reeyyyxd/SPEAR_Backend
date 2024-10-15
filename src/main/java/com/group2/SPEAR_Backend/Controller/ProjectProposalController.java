package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.ProjectProposalEntity;
import com.group2.SPEAR_Backend.Service.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projectProposals")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectProposalController {

    @Autowired
    ProjectProposalService projectProposalService;

    // Create a new project proposal
    @PostMapping("/create")
    public ProjectProposalEntity createProjectProposal(@RequestBody ProjectProposalEntity proposal) {
        return projectProposalService.createProjectProposal(proposal);
    }

    // Get all project proposals
    @GetMapping("/all")
    public List<ProjectProposalEntity> getAllProjectProposals() {
        return projectProposalService.getAllProjectProposals();
    }

    // Update a project proposal by id
    @PutMapping("/update/{id}")
    public ProjectProposalEntity updateProjectProposal(@PathVariable int id, @RequestBody ProjectProposalEntity updatedProposal) {
        return projectProposalService.updateProjectProposal(id, updatedProposal);
    }

    // Delete a project proposal by id
    @DeleteMapping("/delete/{id}")
    public String deleteProjectProposal(@PathVariable int id) {
        return projectProposalService.deleteProjectProposal(id);
    }
}
