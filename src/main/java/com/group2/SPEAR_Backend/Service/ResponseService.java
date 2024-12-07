package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.Response;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository rRepo;

    @Autowired
    private EvaluationRepository eRepo;

    public void submitResponses(List<Response> responses) {
        responses.forEach(response -> {
            if (response.getEvaluator() == null || response.getEvaluatee() == null ||
                    response.getQuestion() == null || response.getEvaluation() == null) {
                throw new IllegalArgumentException("Incomplete response data");
            }
        });

        // Save all responses
        rRepo.saveAll(responses);

        // After saving, check if all evaluators have submitted for the given evaluation
        if (!responses.isEmpty()) {
            Long evaluationId = responses.get(0).getEvaluation().getEid(); // Get the evaluation ID
            checkAndUpdateEvaluationStatus(evaluationId);
        }
    }

    private void checkAndUpdateEvaluationStatus(Long evaluationId) {
        // Fetch the evaluation
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found with ID: " + evaluationId));

        // Fetch all responses for this evaluation
        List<Response> responsesForEvaluation = rRepo.findAll()
                .stream()
                .filter(response -> response.getEvaluation().getEid().equals(evaluationId))
                .toList();

        // Determine if all evaluatees have been evaluated
        long totalEvaluatees = responsesForEvaluation.stream()
                .map(Response::getEvaluatee) // Get all evaluatees
                .distinct() // Remove duplicates
                .count();

        // Fetch the expected number of evaluatees (if available from your team logic)
        long expectedEvaluatees = getExpectedEvaluateeCountForEvaluation(evaluationId);

        // If all evaluatees are evaluated, mark the evaluation as "Completed"
        if (totalEvaluatees >= expectedEvaluatees) {
            evaluation.setStatus("Completed");
            eRepo.save(evaluation);
        }
    }

    private long getExpectedEvaluateeCountForEvaluation(Long evaluationId) {
        // This is a placeholder logic to fetch the expected number of evaluatees for the evaluation.
        // Update this based on how you track the expected number of evaluatees per evaluation.
        // For example, you can fetch it from the associated team or class.
        return 5; // Example: Expecting 5 evaluatees per evaluation
    }

    public List<Response> getResponsesByEvaluator(int evaluatorId) {
        return rRepo.findByEvaluatorUid(evaluatorId);
    }

    public List<Response> getResponsesByEvaluatee(int evaluateeId) {
        return rRepo.findByEvaluateeUid(evaluateeId);
    }

    public double calculateAverageResponse(int evaluateeId) {
        List<Response> responses = getResponsesByEvaluatee(evaluateeId);
        if (responses.isEmpty()) {
            throw new RuntimeException("No responses found for evaluatee ID: " + evaluateeId);
        }
        int totalQuestions = responses.size();
        double totalScore = responses.stream().mapToDouble(Response::getScore).sum();
        return totalScore / totalQuestions;
    }

    public String deleteResponse(Long rid) {
        if (rRepo.existsById(rid)) {
            rRepo.deleteById(rid);
            return "Response deleted successfully";
        } else {
            throw new RuntimeException("Response not found with ID: " + rid);
        }
    }
}
