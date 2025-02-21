package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173"})
public class EvaluationController {

    @Autowired
    private EvaluationService eServ;

    @PostMapping("teacher/create-evaluation/{classId}")
    public ResponseEntity<?> createEvaluation(@RequestBody Evaluation evaluation, @PathVariable Long classId) {
        try {
            EvaluationDTO savedEvaluation = eServ.createEvaluation(evaluation, classId);
            return ResponseEntity.ok(savedEvaluation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


    //for class
    @GetMapping("/teacher/class/{classId}/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByClass(@PathVariable Long classId) {
        List<EvaluationDTO> evaluations = eServ.getEvaluationsByClassAsDTO(classId);
        return ResponseEntity.ok(evaluations);
    }

    //filter by open and by period
    @GetMapping("student/check-availability/{availability}")
    public List<Evaluation> getEvaluationsByAvailability(@PathVariable String availability) {
        return eServ.getEvaluationsByAvailability(availability);
    }

    @PutMapping("teacher/update-evaluation/{id}")
    public ResponseEntity<?> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation updatedEvaluation) {
        try {
            EvaluationDTO response = eServ.updateEvaluation(id, updatedEvaluation);
            return ResponseEntity.ok(response); // Return filtered DTO
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("teacher/delete-evaluation/{id}")
    public String deleteEvaluation(@PathVariable Long id) {
        return eServ.deleteEvaluation(id);
    }

    @GetMapping("/student/evaluation-period/{period}")
    public List<Evaluation> getEvaluationsByPeriod(@PathVariable String period) {
        return eServ.getEvaluationsByPeriod(period);
    }

    @GetMapping("/student/availability/{availability}/period/{period}")
    public List<Evaluation> getEvaluationsByAvailabilityAndPeriod(
            @PathVariable String availability, @PathVariable String period) {
        return eServ.getEvaluationsByAvailabilityAndPeriod(availability, period);
    }

    @GetMapping("/evaluation/{evaluationId}/details")
    public ResponseEntity<EvaluationDTO> getEvaluationDetails(@PathVariable Long evaluationId) {
        EvaluationDTO evaluationDetails = eServ.getEvaluationDetailsById(evaluationId);
        return ResponseEntity.ok(evaluationDetails);
    }
}
