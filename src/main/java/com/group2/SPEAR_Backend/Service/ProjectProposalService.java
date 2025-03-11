package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectProposalService {

    @Autowired
    private ProjectProposalRepository ppRepo;

    @Autowired
    private ClassesRepository cRepo;

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private FeatureRepository fRepo;

    @Autowired
    private TeamRepository tRepo;


    @Transactional
    public ProjectProposal createProjectProposal(ProjectProposalDTO dto, List<FeatureDTO> features) {
        User user = uRepo.findById(dto.getProposedById())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getProposedById()));

        Classes clazz = cRepo.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + dto.getClassId()));

        Team team = dto.getTeamId() != null
                ? tRepo.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found with ID: " + dto.getTeamId()))
                : null;

        // Create new proposal
        ProjectProposal proposal = new ProjectProposal(dto.getProjectName(), dto.getDescription(), user, clazz, team);
        ProjectProposal savedProposal = ppRepo.save(proposal);

        // Save features if provided
        if (features != null && !features.isEmpty()) {
            List<Feature> featureEntities = features.stream()
                    .map(feature -> new Feature(feature.getFeatureTitle(), feature.getFeatureDescription(), savedProposal))
                    .toList();
            fRepo.saveAll(featureEntities);
        }
        return savedProposal;
    }

    private ProjectProposalDTO mapProposalToDTO(ProjectProposal proposal) {
        List<FeatureDTO> features = fRepo.findByProjectId(proposal.getPid()).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();

        return new ProjectProposalDTO(
                proposal.getPid(),
                proposal.getProjectName(),
                proposal.getDescription(),
                proposal.getStatus().name(),
                proposal.getReason(),
                proposal.getProposedBy().getUid(),
                proposal.getClassProposal().getCid(),
                proposal.getTeamProject() != null ? proposal.getTeamProject().getTid() : null,
                features,
                proposal.getProposedBy().getFirstname() + " " + proposal.getProposedBy().getLastname(),
                proposal.getTeamProject() != null ? proposal.getTeamProject().getGroupName() : null
        );
    }

    /**
     * Update project proposal details and features.
     */
    @Transactional
    public void updateProposalAndFeatures(int proposalId, String projectName, String description, List<FeatureDTO> features) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + proposalId + " not found"));

        if (!ProjectStatus.PENDING.name().equalsIgnoreCase(proposal.getStatus().name())) {
            throw new IllegalArgumentException("Only proposals with status 'PENDING' can be updated.");
        }

        if (projectName != null && !projectName.isBlank()) {
            proposal.setProjectName(projectName);
        }
        if (description != null && !description.isBlank()) {
            proposal.setDescription(description);
        }

        // Update features
        if (features != null) {
            List<Feature> existingFeatures = fRepo.findByProjectId(proposalId);
            fRepo.deleteAll(existingFeatures); // Remove old features

            List<Feature> newFeatures = features.stream()
                    .map(f -> new Feature(f.getFeatureTitle(), f.getFeatureDescription(), proposal))
                    .toList();

            fRepo.saveAll(newFeatures); // Save updated features
        }

        ppRepo.save(proposal);
    }


    private ProjectProposalDTO mapProposalToDTOWithFeatures(ProjectProposal proposal) {
        List<FeatureDTO> features = fRepo.findByProjectId(proposal.getPid()).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();

        String courseCode = proposal.getClassProposal().getCourseCode();

        return new ProjectProposalDTO(
                proposal.getPid(),
                proposal.getProjectName(),
                proposal.getDescription(),
                proposal.getStatus().name(),
                proposal.getReason(),
                proposal.getProposedBy().getUid(),
                proposal.getClassProposal().getCid(),
                proposal.getTeamProject() != null ? proposal.getTeamProject().getTid() : null,
                features,
                proposal.getProposedBy().getFirstname() + " " + proposal.getProposedBy().getLastname(),
                proposal.getTeamProject() != null ? proposal.getTeamProject().getGroupName() : null
        );
    }

    //get all active proposals
    public List<ProjectProposalDTO> getAllActiveProposals() {
        return ppRepo.findAllActive().stream()
                .map(this::mapProposalToDTOWithFeatures)
                .toList();
    }

    public List<ProjectProposalDTO> getProposalsByClassAndStudent(Long classId, int studentId) {
        return ppRepo.findByClassAndStudent(classId, studentId).stream().map(this::mapProposalToDTO).toList();
    }

    public List<ProjectProposalDTO> getProposalsByAdviser(int adviserId) {
        return ppRepo.findByAdviser(adviserId).stream().map(this::mapProposalToDTO).toList();
    }

    public List<ProjectProposalDTO> getProposalsByStatus(String status) {
        return ppRepo.findByStatus(status).stream().map(this::mapProposalToDTO).toList();
    }



    //update the proposal
    public ProjectProposal updateProposalStatus(int id, String status, String reason) {
        ProjectProposal proposal = ppRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + id + " not found"));

        if (ProjectStatus.APPROVED.name().equalsIgnoreCase(status)) {
            proposal.setStatus(ProjectStatus.APPROVED);
            proposal.setReason(null);
        } else if (ProjectStatus.DENIED.name().equalsIgnoreCase(status)) {
            if (reason == null || reason.isBlank()) {
                throw new RuntimeException("Reason is required when denying a proposal");
            }
            proposal.setStatus(ProjectStatus.DENIED);
            proposal.setReason(reason);
        } else {
            throw new IllegalArgumentException("Invalid status. Only 'APPROVED' or 'DENIED' are allowed.");
        }

        return ppRepo.save(proposal);
    }

  //delete a proposal
    public String deleteProjectProposal(int id) {
        ProjectProposal proposal = ppRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + id + " not found"));

        if (!proposal.getIsDeleted()) {
            proposal.setDeleted(true);
            ppRepo.save(proposal);
            return "Project proposal with ID " + id + " has been deleted.";
        } else {
            return "Project proposal with ID " + id + " is already deleted.";
        }
    }

    //denied to pending
    @Transactional
    public void updateDeniedToPending(int proposalId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        if (ProjectStatus.DENIED.name().equalsIgnoreCase(proposal.getStatus().name())) {
            proposal.setStatus(ProjectStatus.PENDING);
            proposal.setReason(null);
            ppRepo.save(proposal);
        } else {
            throw new RuntimeException("Proposal is not in DENIED status");
        }
    }

   //approved to open
    @Transactional
    public void updateApprovedToOpenProject(int proposalId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        if (ProjectStatus.APPROVED.name().equalsIgnoreCase(proposal.getStatus().name())) {
            proposal.setStatus(ProjectStatus.OPEN_PROJECT);
            ppRepo.save(proposal);
        } else {
            throw new RuntimeException("Proposal is not in APPROVED status");
        }
    }
}