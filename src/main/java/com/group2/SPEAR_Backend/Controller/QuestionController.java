package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class QuestionController {

    @Autowired
    private QuestionService qServ;

    @PostMapping("/teacher/create-question/{classId}")
    public Question createQuestion(@PathVariable Long classId, @RequestBody Question question) {
        return qServ.createQuestion(classId, question);
    }

    @GetMapping("/get-all-questions-by-class/{classId}")
    public List<Question> getQuestionsByClass(@PathVariable Long classId) {
        return qServ.getQuestionsByClass(classId);
    }

    @PutMapping("/teacher/update-question/{questionId}")
    public Question updateQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
        return qServ.updateQuestion(questionId, updatedQuestion);
    }

    @DeleteMapping("/teacher/delete-question/{questionId}")
    public String deleteQuestion(@PathVariable Long questionId) {
        return qServ.deleteQuestion(questionId);
    }
}
