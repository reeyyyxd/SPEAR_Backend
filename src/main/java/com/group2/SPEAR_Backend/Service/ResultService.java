package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.Response;
import com.group2.SPEAR_Backend.Model.Result;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.ResponseRepository;
import com.group2.SPEAR_Backend.Repository.ResultRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private ResponseRepository responseRepo;

    @Autowired
    private EvaluationRepository evaluationRepo;

    @Autowired
    private UserRepository userRepo;

    public Result calculateAndSaveResult(Long evaluationId, int evaluateeId) {
        Evaluation evaluation = evaluationRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));

        User evaluatee = userRepo.findById(evaluateeId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + evaluateeId));

        Optional<Result> existingResult = resultRepo.findByEvaluationEidAndEvaluateeUid(evaluationId, evaluateeId);
        if (existingResult.isPresent()) {
            return existingResult.get(); // Return the existing result
        }
        List<Response> responses = responseRepo.findByEvaluateeUid(evaluateeId);
        double totalScore = responses.stream()
                .filter(response -> response.getEvaluation().getEid().equals(evaluationId))
                .mapToDouble(Response::getScore)
                .sum();
        double averageScore = responses.size() > 0 ? totalScore / responses.size() : 0.0;

        Result result = new Result(evaluatee, evaluation, averageScore);
        return resultRepo.save(result);
    }


    public List<Result> getResultsByEvaluatee(int evaluateeId) {
        return resultRepo.findByEvaluateeUid(evaluateeId);
    }

    public List<Result> getResultsByEvaluation(Long evaluationId) {
        return resultRepo.findByEvaluationEid(evaluationId);
    }
}
