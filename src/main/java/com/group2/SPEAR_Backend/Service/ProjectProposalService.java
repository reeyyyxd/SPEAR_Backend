package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectProposalService {

    @Autowired
    ProjectProposalRepository projectProposalRepository;

    // Create new project proposal
    public ProjectProposal createProjectProposal(ProjectProposal proposal) {
        return projectProposalRepository.save(proposal);
    }

    // Get all project proposals
    public List<ProjectProposal> getAllProjectProposals() {
        return projectProposalRepository.findAll();
    }

    // Update project proposal
    public ProjectProposal updateProjectProposal(int id, ProjectProposal updatedProposal) {
        try {
            ProjectProposal existingProposal = projectProposalRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Project proposal with id " + id + " not found"));

            existingProposal.setProjectName(updatedProposal.getProjectName());
            existingProposal.setDescription(updatedProposal.getDescription());
            existingProposal.setStatus(updatedProposal.getStatus());
            existingProposal.setReason(updatedProposal.getReason());

            return projectProposalRepository.save(existingProposal);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Project proposal with id " + id + " not found");
        }
    }

    // Delete project proposal
    public String deleteProjectProposal(int id) {
        if (projectProposalRepository.existsById(id)) {
            projectProposalRepository.deleteById(id);
            return "Project proposal with id " + id + " deleted";
        } else {
            return "Project proposal with id " + id + " not found";
        }
    }
}
