package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.FeatureRepository;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
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

    public ProjectProposal createProjectProposal(ProjectProposalDTO dto) {
        User user = uRepo.findById(dto.getProposedById())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getProposedById()));

        Classes clazz = cRepo.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + dto.getClassId()));

        User adviser = null;

        if ("CAPSTONE".equalsIgnoreCase(clazz.getCourseType())) {
            if (dto.getAdviserId() == null) {
                throw new RuntimeException("Adviser is required for capstone classes");
            }
            adviser = uRepo.findById(dto.getAdviserId())
                    .filter(u -> "TEACHER".equalsIgnoreCase(u.getRole()))
                    .orElseThrow(() -> new RuntimeException("Adviser with ID " + dto.getAdviserId() + " not found or is not a teacher"));
        }
        ProjectProposal proposal = new ProjectProposal(user, dto.getProjectName(), clazz, dto.getDescription(), adviser);
        return ppRepo.save(proposal);
    }

    public List<ProjectProposal> getAllActiveProposals() {
        return ppRepo.findAllActive();
    }

    public ProjectProposal updateProposalStatus(int id, String status, String reason) {
        ProjectProposal proposal = ppRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + id + " not found"));

        if ("APPROVED".equalsIgnoreCase(status)) {
            proposal.setStatus("APPROVED");
            proposal.setReason(null);
        } else if ("DENIED".equalsIgnoreCase(status)) {
            if (reason == null || reason.isBlank()) {
                throw new RuntimeException("Reason is required when denying a proposal");
            }
            proposal.setStatus("DENIED");
            proposal.setReason(reason);
        } else {
            throw new IllegalArgumentException("Invalid status. Only 'APPROVED' or 'DENIED' are allowed.");
        }

        return ppRepo.save(proposal);
    }

    public String deleteProjectProposal(int id) {
        ProjectProposal proposal = ppRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + id + " not found"));

        if (!proposal.getIsDeleted()) {
            proposal.setDeleted(true);
            ppRepo.save(proposal);
            return "Project proposal with ID " + id + " has been soft deleted.";
        } else {
            return "Project proposal with ID " + id + " is already deleted.";
        }
    }

    public List<ProjectProposalDTO> getProposalsByClassWithFeatures(Long classId) {
        List<ProjectProposal> proposals = ppRepo.findByClassIdNotDeleted(classId);
        return proposals.stream()
                .map(this::mapProposalToDTOWithFeatures)
                .toList();
    }

    private ProjectProposalDTO mapProposalToDTOWithFeatures(ProjectProposal proposal) {
        List<FeatureDTO> features = fRepo.findByProjectId(proposal.getPid()).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();

        return new ProjectProposalDTO(
                proposal.getPid(),
                proposal.getProjectName(),
                proposal.getDescription(),
                proposal.getStatus(),
                proposal.getReason(),
                proposal.getProposedBy().getUid(),
                proposal.getClassProposal().getCid(),
                proposal.getAdviser() != null ? proposal.getAdviser().getUid() : null,
                proposal.getIsDeleted(),
                features
        );
    }
}
