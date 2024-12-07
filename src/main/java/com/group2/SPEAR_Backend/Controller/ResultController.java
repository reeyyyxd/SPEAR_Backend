package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.Result;
import com.group2.SPEAR_Backend.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @PostMapping("/teacher/calculate-result")
    public Result calculateAndSaveResult(
            @RequestParam Long evaluationId,
            @RequestParam int evaluateeId) {
        return resultService.calculateAndSaveResult(evaluationId, evaluateeId);
    }

    @GetMapping("teacher/by-evaluatee/{evaluateeId}")
    public List<Result> getResultsByEvaluatee(@PathVariable int evaluateeId) {
        return resultService.getResultsByEvaluatee(evaluateeId);
    }

    @GetMapping("teacher/by-evaluation/{evaluationId}")
    public List<Result> getResultsByEvaluation(@PathVariable Long evaluationId) {
        return resultService.getResultsByEvaluation(evaluationId);
    }
}
