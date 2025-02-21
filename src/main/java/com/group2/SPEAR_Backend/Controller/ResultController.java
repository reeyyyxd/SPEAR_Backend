package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ResultDTO;
import com.group2.SPEAR_Backend.Model.Result;
import com.group2.SPEAR_Backend.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173"})
public class ResultController {

    @Autowired
    private ResultService resultService;

    @PostMapping("/teacher/calculate-result")
    public ResultDTO calculateAndSaveResult(
            @RequestParam Long evaluationId,
            @RequestParam int evaluateeId) {
        Result result = resultService.calculateAndSaveResult(evaluationId, evaluateeId);
        String evaluateeName = result.getEvaluatee().getFirstname() + " " + result.getEvaluatee().getLastname();

        return new ResultDTO(
                result.getResultId(),
                result.getEvaluatee().getUid(),
                evaluateeName, // Include evaluatee name
                result.getEvaluation().getEid(),
                result.getAverageScore()
        );
    }


    @GetMapping("teacher/by-evaluatee/{evaluateeId}")
    public List<ResultDTO> getResultsByEvaluatee(@PathVariable int evaluateeId) {
        return resultService.getResultsByEvaluatee(evaluateeId);
    }

    @GetMapping("teacher/by-evaluation/{evaluationId}")
    public List<ResultDTO> getResultsByEvaluation(@PathVariable Long evaluationId) {
        return resultService.getResultsByEvaluation(evaluationId);
    }
}
