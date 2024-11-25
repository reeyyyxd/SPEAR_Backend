package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Service.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projectProposals")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectProposalController {

    @Autowired
    ProjectProposalService projectProposalService;

    // Create a new project proposal
    @PostMapping("/create")
    public ProjectProposal createProjectProposal(@RequestBody ProjectProposal proposal) {
        return projectProposalService.createProjectProposal(proposal);
    }

    // Get all project proposals
    @GetMapping("/all")
    public List<ProjectProposal> getAllProjectProposals() {
        return projectProposalService.getAllProjectProposals();
    }

    // Update a project proposal by id
    @PutMapping("/update/{id}")
    public ProjectProposal updateProjectProposal(@PathVariable int id, @RequestBody ProjectProposal updatedProposal) {
        return projectProposalService.updateProjectProposal(id, updatedProposal);
    }

    // Delete a project proposal by id
    @DeleteMapping("/delete/{id}")
    public String deleteProjectProposal(@PathVariable int id) {
        return projectProposalService.deleteProjectProposal(id);
    }
}
