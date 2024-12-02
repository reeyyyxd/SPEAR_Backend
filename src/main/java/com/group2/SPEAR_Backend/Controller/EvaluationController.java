package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class EvaluationController {

    @Autowired
    private EvaluationService eServ;

    @PostMapping("/teacher/create")
    public Evaluation createEvaluation(@RequestBody Evaluation evaluation, @RequestParam Long classId, @RequestParam Long evaluatorId) {
        return eServ.createEvaluation(evaluation, classId, evaluatorId);
    }

    //for class
    @GetMapping("/teacher/all")
    public List<Evaluation> getAllEvaluations() {
        return eServ.getAllEvaluations();
    }
//filter by open and by period
    @GetMapping("student/availability/{availability}")
    public List<Evaluation> getEvaluationsByAvailability(@PathVariable String availability) {
        return eServ.getEvaluationsByAvailability(availability);
    }

    @PutMapping("teacher/update/{id}")
    public Evaluation updateEvaluation(@PathVariable Long id, @RequestBody Evaluation updatedEvaluation) {
        return eServ.updateEvaluation(id, updatedEvaluation);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEvaluation(@PathVariable Long id) {
        return eServ.deleteEvaluation(id);
    }

    @GetMapping("/student/period/{period}")
    public List<Evaluation> getEvaluationsByPeriod(@PathVariable String period) {
        return eServ.getEvaluationsByPeriod(period);
    }

    @GetMapping("/student/availability/{availability}/period/{period}")
    public List<Evaluation> getEvaluationsByAvailabilityAndPeriod(
            @PathVariable String availability, @PathVariable String period) {
        return eServ.getEvaluationsByAvailabilityAndPeriod(availability, period);
    }
}
