package com.group2.SPEAR_Backend.Controller;

import java.util.List;

import com.group2.SPEAR_Backend.DTO.FeatureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group2.SPEAR_Backend.Model.Feature;
import com.group2.SPEAR_Backend.Service.FeatureService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://10.147.17.166:5173" ,"http://172.23.234.170:5173" })
public class FeatureController {

    @Autowired
    private FeatureService fServ;

    @PostMapping("/addFeature")
    public ResponseEntity<String> addMultipleFeatures(@RequestBody List<FeatureDTO> features, @RequestParam int projectId) {
        fServ.addFeature(features, projectId);
        return ResponseEntity.ok("Features added successfully.");
    }
    @GetMapping("/getAllFeatures")
    public List<Feature> getAllFeatures() {
        return fServ.getAllFeatures();
    }

    @GetMapping("/features/project/{projectId}")
    public List<FeatureDTO> getFeaturesByProject(@PathVariable int projectId) {
        return fServ.getFeaturesByProjectId(projectId).stream()
                .map(feature -> new FeatureDTO(feature.getFeatureTitle(), feature.getFeatureDescription()))
                .toList();
    }

}
