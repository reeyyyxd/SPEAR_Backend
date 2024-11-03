package com.group2.SPEAR_Backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.group2.SPEAR_Backend.Entity.FeatureEntity;
import com.group2.SPEAR_Backend.Service.FeatureService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @PostMapping("/addFeature")
    public FeatureEntity addFeature(@RequestBody FeatureEntity feature, @RequestParam int projectId) {
        return featureService.addFeature(feature, projectId);
    }

    @GetMapping("/getAllFeatures")
    public List<FeatureEntity> getAllFeatures() {
        return featureService.getAllFeatures();
    }
}
