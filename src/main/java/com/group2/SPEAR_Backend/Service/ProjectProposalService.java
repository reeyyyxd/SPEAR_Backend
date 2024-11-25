package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectProposalService {

    @Autowired
    private ProjectProposalRepository ppRepo;

    @Autowired
    private ClassesRepository classesRepo;

    @Autowired
    private UserRepository userRepo;

    public ProjectProposal createProjectProposal(ProjectProposalDTO dto) {
        User user = userRepo.findById(dto.getProposedById())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getProposedById()));

        Classes clazz = classesRepo.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + dto.getClassId()));

        ProjectProposal proposal = new ProjectProposal(user, dto.getProjectName(), clazz, dto.getDescription());
        return ppRepo.save(proposal);
    }

    public List<ProjectProposal> getAllProjectProposals() {
        return ppRepo.findAll();
    }

    public ProjectProposal updateProposalStatus(int id, String status, String reason) {
        ProjectProposal proposal = ppRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + id + " not found"));

        proposal.setStatus(status);
        proposal.setReason(reason);

        return ppRepo.save(proposal);
    }

    public String deleteProjectProposal(int id) {
        if (ppRepo.existsById(id)) {
            ppRepo.deleteById(id);
            return "Project proposal with ID " + id + " deleted";
        } else {
            return "Project proposal with ID " + id + " not found";
        }
    }
}
