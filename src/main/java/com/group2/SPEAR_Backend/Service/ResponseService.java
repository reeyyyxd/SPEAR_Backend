package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Response;
import com.group2.SPEAR_Backend.Repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository rRepo;

    public void submitResponses(List<Response> responses) {
        responses.forEach(response -> {
            if (response.getEvaluator() == null || response.getEvaluatee() == null ||
                    response.getQuestion() == null || response.getEvaluation() == null) {
                throw new IllegalArgumentException("Incomplete response data");
            }
        });
        rRepo.saveAll(responses);
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
