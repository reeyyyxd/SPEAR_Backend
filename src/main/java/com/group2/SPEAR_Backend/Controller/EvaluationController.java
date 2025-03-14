package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.EvaluationType;
import com.group2.SPEAR_Backend.Service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173", "http://10.147.17.166:5173"})
public class EvaluationController {

    @Autowired
    private EvaluationService eServ;

    /**
     * Create an evaluation (Teacher creates evaluations)
     */
    @PostMapping("teacher/create-evaluation/{classId}")
    public ResponseEntity<?> createEvaluation(
            @RequestBody Evaluation evaluation,
            @PathVariable Long classId) {

        try {
            if (evaluation.getEvaluationType() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Evaluation type is required."));
            }

            EvaluationDTO savedEvaluation = eServ.createEvaluation(evaluation, classId, evaluation.getEvaluationType());
            return ResponseEntity.ok(savedEvaluation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get evaluations for a specific class
     */
    @GetMapping("/teacher/class/{classId}/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByClass(@PathVariable Long classId) {
        List<EvaluationDTO> evaluations = eServ.getEvaluationsByClassAsDTO(classId);
        return ResponseEntity.ok(evaluations);
    }

    /**
     * Get evaluations by availability (e.g., Open or Closed)
     */
    @GetMapping("student/check-availability/{availability}")
    public List<Evaluation> getEvaluationsByAvailability(@PathVariable String availability) {
        return eServ.getEvaluationsByAvailability(availability);
    }

    /**
     * Get evaluations by period (Prelims, Midterms, etc.)
     */
    @GetMapping("/student/evaluation-period/{period}")
    public List<Evaluation> getEvaluationsByPeriod(@PathVariable String period) {
        return eServ.getEvaluationsByPeriod(period);
    }

    /**
     * Get evaluations by availability and period
     */
    @GetMapping("/student/availability/{availability}/period/{period}")
    public List<Evaluation> getEvaluationsByAvailabilityAndPeriod(
            @PathVariable String availability, @PathVariable String period) {
        return eServ.getEvaluationsByAvailabilityAndPeriod(availability, period);
    }

    /**
     * Get evaluations by type (STUDENT_TO_STUDENT, STUDENT_TO_ADVISER, etc.)
     */
    @GetMapping("/evaluation/type/{evaluationType}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByType(@PathVariable EvaluationType evaluationType) {
        List<Evaluation> evaluations = eServ.getEvaluationsByType(evaluationType);
        return ResponseEntity.ok(evaluations);
    }

    /**
     * Get evaluation details (including team name, evaluator/evaluatee names, adviser name)
     */
    @GetMapping("/evaluation/{evaluationId}/details")
    public ResponseEntity<EvaluationDTO> getEvaluationDetails(@PathVariable Long evaluationId) {
        EvaluationDTO evaluationDetails = eServ.getEvaluationDetailsById(evaluationId);
        return ResponseEntity.ok(evaluationDetails);
    }

    /**
     * Update evaluation details
     */
    @PutMapping("teacher/update-evaluation/{id}")
    public ResponseEntity<?> updateEvaluation(
            @PathVariable Long id,
            @RequestBody Evaluation updatedEvaluation) {

        try {
            EvaluationDTO response = eServ.updateEvaluation(id, updatedEvaluation, updatedEvaluation.getEvaluationType());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete evaluation
     */
    @DeleteMapping("teacher/delete-evaluation/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        try {
            String result = eServ.deleteEvaluation(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}