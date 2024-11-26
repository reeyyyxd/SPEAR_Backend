package com.group2.SPEAR_Backend.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group2.SPEAR_Backend.Model.Feature;
import com.group2.SPEAR_Backend.Model.ProjectProposal;
import com.group2.SPEAR_Backend.Repository.FeatureRepository;
import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository fRepo;

    @Autowired
    private ProjectProposalRepository ppRepo;

    public Feature addFeature(Feature feature, int projectId) {
        Optional<ProjectProposal> optionalProjectProposal = ppRepo.findById(projectId);

        if (optionalProjectProposal.isPresent()) {
            ProjectProposal project = optionalProjectProposal.get();
            feature.setProject(project);
            return fRepo.save(feature);
        } else {
            throw new RuntimeException("Project Proposal not found with ID: " + projectId);
        }
    }

    public List<Feature> getAllFeatures() {
        return fRepo.findAll();
    }

    public List<Feature> getFeaturesByProjectId(int projectId) {
        return fRepo.findByProjectId(projectId);
    }
}
