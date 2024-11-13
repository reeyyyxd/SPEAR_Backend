//package com.group2.SPEAR_Backend.Controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.group2.SPEAR_Backend.Model.Feature;
//import com.group2.SPEAR_Backend.Service.FeatureService;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:5173")
//public class FeatureController {
//
//    @Autowired
//    private FeatureService featureService;
//
//    @PostMapping("/addFeature")
//    public Feature addFeature(@RequestBody Feature feature, @RequestParam int projectId) {
//        return featureService.addFeature(feature, projectId);
//    }
//
//    @GetMapping("/getAllFeatures")
//    public List<Feature> getAllFeatures() {
//        return featureService.getAllFeatures();
//    }
//}
