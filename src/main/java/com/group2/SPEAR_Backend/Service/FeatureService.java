//package com.group2.SPEAR_Backend.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.group2.SPEAR_Backend.Model.Feature;
//import com.group2.SPEAR_Backend.Model.ProjectProposal;
//import com.group2.SPEAR_Backend.Repository.FeatureRepository;
//import com.group2.SPEAR_Backend.Repository.ProjectProposalRepository;
//
//@Service
//public class FeatureService {
//
//    @Autowired
//    private FeatureRepository featureRepository;
//
//    @Autowired
//    private ProjectProposalRepository projectProposalRepository;
//
//    public Feature addFeature(Feature feature, int projectId) {
//        Optional<ProjectProposal> optionalProjectProposal = projectProposalRepository.findById(projectId);
//
//        if (optionalProjectProposal.isPresent()) {
//            feature.setProject(optionalProjectProposal.get()); // Set the project proposal for this feature
//            return featureRepository.save(feature);
//        } else {
//            throw new RuntimeException("Project Proposal not found with ID: " + projectId);
//        }
//    }
//
//    public List<Feature> getAllFeatures() {
//        return featureRepository.findAll();
//    }
//}
