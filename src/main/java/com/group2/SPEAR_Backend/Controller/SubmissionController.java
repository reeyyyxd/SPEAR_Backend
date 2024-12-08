package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.Submission;
import com.group2.SPEAR_Backend.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@CrossOrigin(origins = "http://localhost:5173")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/submit")
    public ResponseEntity<Submission> createSubmission(
            @RequestParam Long evaluationId,
            @RequestParam int evaluatorId) {
        Submission submission = submissionService.createSubmission(evaluationId, evaluatorId);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/by-evaluation/{evaluationId}")
    public List<Submission> getSubmissionsByEvaluation(@PathVariable Long evaluationId) {
        return submissionService.getSubmissionsByEvaluation(evaluationId);
    }

    @GetMapping("/by-evaluator/{evaluatorId}")
    public List<Submission> getSubmissionsByEvaluator(@PathVariable int evaluatorId) {
        return submissionService.getSubmissionsByEvaluator(evaluatorId);
    }

    @GetMapping("/by-status/{status}")
    public List<Submission> getSubmissionsByStatus(@PathVariable String status) {
        return submissionService.getSubmissionsByStatus(status);
    }
}