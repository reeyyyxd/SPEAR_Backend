package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.EvaluationEntity;
import com.group2.SPEAR_Backend.Service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
@CrossOrigin(origins = "http://localhost:3000")
public class EvaluationController {

    @Autowired
    EvaluationService evaluationService;

    // Create a new evaluation
    @PostMapping("/create")
    public EvaluationEntity createEvaluation(@RequestBody EvaluationEntity evaluation) {
        return evaluationService.createEvaluation(evaluation);
    }

    // Get all evaluations
    @GetMapping("/all")
    public List<EvaluationEntity> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    // Update an evaluation by id
    @PutMapping("/update/{id}")
    public EvaluationEntity updateEvaluation(@PathVariable int id, @RequestBody EvaluationEntity updatedEvaluation) {
        return evaluationService.updateEvaluation(id, updatedEvaluation);
    }

    // Delete an evaluation by id
    @DeleteMapping("/delete/{id}")
    public String deleteEvaluation(@PathVariable int id) {
        return evaluationService.deleteEvaluation(id);
    }
}
