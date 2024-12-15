package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import com.group2.SPEAR_Backend.DTO.ProjectProposalDTO;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Feature;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.FeatureRepository;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
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

    public ProjectProposal createProjectProposal(ProjectProposalDTO dto, List<FeatureDTO> features) {
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
        ProjectProposal savedProposal = ppRepo.save(proposal);

        if (features != null && !features.isEmpty()) {
            List<Feature> featureEntities = features.stream()
                    .map(feature -> new Feature(feature.getFeatureTitle(), feature.getFeatureDescription(), savedProposal))
                    .toList();

            fRepo.saveAll(featureEntities);
        }

        return savedProposal;
    }

    public List<ProjectProposal> getAllActiveProposals() {
        return ppRepo.findAllActive();
    }

    public void updateProposalAndFeatures(int proposalId, String projectName, String description, List<FeatureDTO> features) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + proposalId + " not found"));

        if (!"PENDING".equalsIgnoreCase(proposal.getStatus())) {
            throw new IllegalArgumentException("Only proposals with status 'PENDING' can be updated.");
        }

        if (projectName != null && !projectName.isBlank()) {
            proposal.setProjectName(projectName);
        }
        if (description != null && !description.isBlank()) {
            proposal.setDescription(description);
        }

        if (features != null) {
            List<Feature> existingFeatures = fRepo.findByProjectId(proposalId);
            fRepo.deleteAll(existingFeatures);

            for (FeatureDTO featureDTO : features) {
                Feature feature = new Feature(featureDTO.getFeatureTitle(), featureDTO.getFeatureDescription(), proposal);
                fRepo.save(feature);
            }
        }

        ppRepo.save(proposal);
    }

    public void updateCapstoneAdviser(int proposalId, int adviserId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Project proposal with ID " + proposalId + " not found"));

        if (!"CAPSTONE".equalsIgnoreCase(proposal.getClassProposal().getCourseType())) {
            throw new IllegalArgumentException("Adviser can only be updated for Capstone projects.");
        }
        User adviser = uRepo.findById(adviserId)
                .filter(user -> "TEACHER".equalsIgnoreCase(user.getRole()))
                .orElseThrow(() -> new RuntimeException("Adviser with ID " + adviserId + " not found or is not a teacher"));
        proposal.setAdviser(adviser);
        ppRepo.save(proposal);
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
            return "Project proposal with ID " + id + " has been deleted.";
        } else {
            return "Project proposal with ID " + id + " is already deleted.";
        }
    }

    public List<ProjectProposalDTO> getProposalsByClassAndStudent(Long classId, int studentId) {
        List<ProjectProposal> proposals = ppRepo.findByClassAndStudent(classId, studentId);
        return proposals.stream().map(this::mapProposalToDTOWithFeatures).toList();
    }

    public List<ProjectProposalDTO> getProposalsByAdviser(int adviserId) {
        List<ProjectProposal> proposals = ppRepo.findByAdviser(adviserId);
        return proposals.stream().map(this::mapProposalToDTOWithFeatures).toList();
    }

    public List<ProjectProposalDTO> getProposalsByStatus(String status) {
        List<ProjectProposal> proposals = ppRepo.findByStatus(status);
        return proposals.stream().map(this::mapProposalToDTOWithFeatures).toList();
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
    public List<ProjectProposalDTO> getProposalsByClassAndStatus(Long classId, String status) {
        List<ProjectProposal> proposals = ppRepo.findByClassAndStatus(classId, status);
        return proposals.stream().map(this::mapProposalToDTOWithFeatures).toList();
    }


    @Transactional
    public void updateDeniedToPending(int proposalId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        if ("DENIED".equalsIgnoreCase(proposal.getStatus())) {
            proposal.setStatus("PENDING");
            proposal.setReason(null);
            ppRepo.save(proposal);
        } else {
            throw new RuntimeException("Proposal is not in DENIED status");
        }
    }

    @Transactional
    public void updateApprovedToOpenProject(int proposalId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        if ("APPROVED".equalsIgnoreCase(proposal.getStatus())) {
            proposal.setStatus("OPEN PROJECT");
            ppRepo.save(proposal);
        } else {
            throw new RuntimeException("Proposal is not in APPROVED status");
        }
    }

    @Transactional
    public void assignStudentToOpenProject(int proposalId, int studentId) {
        ProjectProposal proposal = ppRepo.findById(proposalId)
                .orElseThrow(() -> new RuntimeException("Proposal not found with ID: " + proposalId));

        if (!"OPEN PROJECT".equalsIgnoreCase(proposal.getStatus())) {
            throw new RuntimeException("Proposal is not in OPEN PROJECT status");
        }

        User student = uRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        proposal.setProposedBy(student);
        proposal.setStatus("APPROVED");
        ppRepo.save(proposal);
    }

    public String getAdviserFullNameByProposalId(int proposalId) {
        return ppRepo.findAdviserFullNameByProposalId(proposalId);
    }

    public String getLeaderNameById(int leaderId) {
        return uRepo.findFullNameById(leaderId);
    }

    public List<ProjectProposalDTO> getProposalsByUser(int userId) {
        List<ProjectProposal> proposals = ppRepo.findAllByProposedBy(userId);
        return proposals.stream()
                .map(this::mapProposalToDTOWithFeatures)
                .toList();
    }


}
