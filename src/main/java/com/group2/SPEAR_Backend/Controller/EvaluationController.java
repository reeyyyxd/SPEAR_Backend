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

    @DeleteMapping("teacher/delete-evaluation/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        try {
            String result = eServ.deleteEvaluation(id);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    //get functions
    //get evaluations for a specific class
    @GetMapping("/teacher/class/{classId}/evaluations")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByClass(@PathVariable Long classId) {
        List<EvaluationDTO> evaluations = eServ.getEvaluationsByClassAsDTO(classId);
        return ResponseEntity.ok(evaluations);
    }

    //get by availability
    @GetMapping("student/check-availability/{availability}")
    public List<Evaluation> getEvaluationsByAvailability(@PathVariable String availability) {
        return eServ.getEvaluationsByAvailability(availability);
    }

    //get by period
    @GetMapping("/student/evaluation-period/{period}")
    public List<Evaluation> getEvaluationsByPeriod(@PathVariable String period) {
        return eServ.getEvaluationsByPeriod(period);
    }

    //get by availability and period
    @GetMapping("/student/availability/{availability}/period/{period}")
    public List<Evaluation> getEvaluationsByAvailabilityAndPeriod(
            @PathVariable String availability, @PathVariable String period) {
        return eServ.getEvaluationsByAvailabilityAndPeriod(availability, period);
    }

    //get by evaluation type
    @GetMapping("/evaluation/type/{evaluationType}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByType(@PathVariable EvaluationType evaluationType) {
        List<Evaluation> evaluations = eServ.getEvaluationsByType(evaluationType);
        return ResponseEntity.ok(evaluations);
    }

    //get members
    @GetMapping("/team/{classId}/my-members")
    public ResponseEntity<String> getYourTeamMembers(@PathVariable Long classId, @RequestParam Long studentId) {
        String members = eServ.getYourTeamMembers(classId, studentId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/student/{studentId}/available-evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAvailableEvaluations(@PathVariable Long studentId) {
        List<EvaluationDTO> evaluations = eServ.getEvaluationsForStudent(studentId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/teacher/{adviserId}/available-evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAvailableEvaluationsForAdviser(@PathVariable Long adviserId) {
        List<EvaluationDTO> evaluations = eServ.getEvaluationsForAdviser(adviserId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{studentId}/team/{classId}")
    public ResponseEntity<Long> getStudentTeamId(@PathVariable Long studentId, @PathVariable Long classId) {
        Long teamId = eServ.getTeamId(studentId, classId);
        if (teamId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(teamId);
    }
    //for admin download
    @GetMapping("/admin/adviser-to-student-evaluations")
    public ResponseEntity<List<EvaluationDTO>> getAllAdviserToStudentEvaluations() {
        List<EvaluationDTO> evaluations = eServ.getAllAdviserToStudentEvaluations();
        return ResponseEntity.ok(evaluations);
    }

//    @GetMapping("/admin/download/adviser-to-student-evaluations/csv")
//    public ResponseEntity<byte[]> downloadAdviserToStudentEvaluationsCSV() {
//        List<EvaluationDTO> evaluations = eServ.getAllAdviserToStudentEvaluations();
//
//        StringBuilder csvData = new StringBuilder("Evaluation ID, Type, Availability, Date Open, Date Close, Period, Course Code, Section, Description\n");
//
//        for (EvaluationDTO eval : evaluations) {
//            csvData.append(eval.getEid()).append(",")
//                    .append(eval.getEvaluationType()).append(",")
//                    .append(eval.getAvailability()).append(",")
//                    .append(eval.getDateOpen()).append(",")
//                    .append(eval.getDateClose()).append(",")
//                    .append(eval.getPeriod()).append(",")
//                    .append(eval.getCourseCode()).append(",")
//                    .append(eval.getSection()).append(",")
//                    .append(eval.getCourseDescription()).append("\n");
//        }
//
//        byte[] csvBytes = csvData.toString().getBytes();
//
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=adviser_to_student_evaluations.csv")
//                .header("Content-Type", "text/csv")
//                .body(csvBytes);
//    }




}