package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.QuestionDTO;
import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173"})
public class QuestionController {

    @Autowired
    private QuestionService qServ;

    @PostMapping("/teacher/create-question/{classId}/{evaluationId}")
    public QuestionDTO createQuestion(
            @PathVariable Long classId,
            @PathVariable Long evaluationId,
            @RequestBody QuestionDTO questionDTO) {
        return qServ.createQuestion(classId, evaluationId, questionDTO);
    }


    @GetMapping("/get-all-questions-by-class/{classId}")
    public List<QuestionDTO> getQuestionsByClass(@PathVariable Long classId) {
        return qServ.getQuestionsByClass(classId);
    }

    @GetMapping("/get-questions-by-evaluation/{evaluationId}")
    public List<QuestionDTO> getQuestionsByEvaluation(@PathVariable Long evaluationId) {
        return qServ.getQuestionsByEvaluation(evaluationId);
    }

    @GetMapping("/get-questions-by-class-and-evaluation/{classId}/{evaluationId}")
    public List<QuestionDTO> getQuestionsByClassAndEvaluation(
            @PathVariable Long classId, @PathVariable Long evaluationId) {
        return qServ.getQuestionsByClassAndEvaluation(classId, evaluationId);
    }

    @PutMapping("/teacher/update-question/{questionId}")
    public QuestionDTO updateQuestion(@PathVariable Long questionId, @RequestBody QuestionDTO updatedQuestionDTO) {
        return qServ.updateQuestion(questionId, updatedQuestionDTO);
    }

    @DeleteMapping("/teacher/delete-question/{questionId}")
    public QuestionDTO deleteQuestion(@PathVariable Long questionId) {
        return qServ.deleteQuestion(questionId);
    }

}

