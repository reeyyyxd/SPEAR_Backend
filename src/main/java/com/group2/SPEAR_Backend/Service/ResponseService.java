package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.ResponseDTO;
import com.group2.SPEAR_Backend.DTO.TeamDTO;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.Response;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.ResponseRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository rRepo;

    @Autowired
    private EvaluationRepository eRepo;

    @Autowired
    private SubmissionService subServ;

    @Autowired
    private TeamService tServ;

    @Autowired
    private ResultService resServ;


    public void submitResponses(List<Response> responses, int teamId) {
        TeamDTO teamDTO = tServ.getTeamById(teamId);
        if (teamDTO == null || teamDTO.getMemberIds().isEmpty()) {
            throw new IllegalArgumentException("Invalid team or no members in the team.");
        }

        List<Integer> validMembers = teamDTO.getMemberIds();

        // Filter valid responses
        List<Response> validResponses = responses.stream()
                .filter(response -> {
                    if (response == null || response.getEvaluator() == null || response.getEvaluatee() == null ||
                            response.getQuestion() == null || response.getEvaluation() == null) {
                        System.out.println("Warning: Skipping invalid response: " + response);
                        return false;
                    }
                    boolean isValidEvaluator = validMembers.contains(response.getEvaluator().getUid());
                    boolean isValidEvaluatee = validMembers.contains(response.getEvaluatee().getUid());
                    if (!isValidEvaluator || !isValidEvaluatee) {
                        System.out.println("Warning: Invalid evaluator or evaluatee in response: " + response);
                    }
                    return isValidEvaluator && isValidEvaluatee;
                })
                .toList();

        if (validResponses.isEmpty()) {
            System.out.println("No valid responses were provided.");
            return;
        }

        // Save valid responses
        rRepo.saveAll(validResponses);

        // Process evaluatees for results
        Long evaluationId = validResponses.get(0).getEvaluation().getEid();
        validResponses.stream()
                .map(Response::getEvaluatee)
                .distinct()
                .forEach(evaluatee -> {
                    int evaluateeId = evaluatee.getUid();
                    resServ.calculateAndSaveResult(evaluationId, evaluateeId);
                });

        // Create submission for the evaluator
        int evaluatorId = validResponses.get(0).getEvaluator().getUid();
        subServ.createSubmission(evaluationId, evaluatorId);

        // Update evaluation status
        checkAndUpdateEvaluationStatus(evaluationId);
    }




    private void checkAndUpdateEvaluationStatus(Long evaluationId) {
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found with ID: " + evaluationId));

        List<Response> responsesForEvaluation = rRepo.findAll()
                .stream()
                .filter(response -> response.getEvaluation().getEid().equals(evaluationId))
                .toList();

        long totalEvaluatees = responsesForEvaluation.stream()
                .map(Response::getEvaluatee)
                .distinct()
                .count();

        long expectedEvaluatees = getExpectedEvaluateeCountForEvaluation(evaluationId);

        if (totalEvaluatees >= expectedEvaluatees) {
            eRepo.save(evaluation);
        }
    }

    private long getExpectedEvaluateeCountForEvaluation(Long evaluationId) {
        return 2; // Placeholder: Adjust based on your team logic
    }

    public List<ResponseDTO> getResponsesByEvaluator(int evaluatorId) {
        return rRepo.findResponsesByEvaluatorId(evaluatorId);
    }

    public List<ResponseDTO> getResponsesByEvaluatee(int evaluateeId) {
        return rRepo.findResponsesByEvaluateeId(evaluateeId);
    }


    public double calculateAverageResponse(int evaluateeId) {
        List<Response> responses = rRepo.findByEvaluateeUid(evaluateeId);
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
