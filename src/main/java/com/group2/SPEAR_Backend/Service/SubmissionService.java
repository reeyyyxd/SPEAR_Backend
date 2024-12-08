package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.Submission;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.SubmissionRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepo;

    @Autowired
    private EvaluationRepository evaluationRepo;

    @Autowired
    private UserRepository userRepo;

    public Submission createSubmission(Long evaluationId, int evaluatorId) {
        Evaluation evaluation = evaluationRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));
        User evaluator = userRepo.findById(evaluatorId)
                .orElseThrow(() -> new NoSuchElementException("Evaluator not found with ID: " + evaluatorId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime evaluationCloseDateTime = evaluation.getDateClose().atTime(23, 59, 59); // Assume end of day for closing

        String status;
        if (now.isAfter(evaluationCloseDateTime)) {
            status = "Late";
        } else {
            status = "Submitted";
        }

        Submission submission = new Submission(evaluation, evaluator, now, status);
        return submissionRepo.save(submission);
    }


    public List<Submission> getSubmissionsByEvaluation(Long evaluationId) {
        return submissionRepo.findByEvaluationEid(evaluationId);
    }

    public List<Submission> getSubmissionsByEvaluator(int evaluatorId) {
        return submissionRepo.findByEvaluatorUid(evaluatorId);
    }

    public List<Submission> getSubmissionsByStatus(String status) {
        return submissionRepo.findByStatus(status);
    }
}
