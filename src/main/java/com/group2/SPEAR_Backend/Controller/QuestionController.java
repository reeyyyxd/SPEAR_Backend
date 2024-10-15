package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.QuestionEntity;
import com.group2.SPEAR_Backend.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    // Create a new question
    @PostMapping("/create")
    public QuestionEntity createQuestion(@RequestBody QuestionEntity question) {
        return questionService.createQuestion(question);
    }

    // Get all questions
    @GetMapping("/all")
    public List<QuestionEntity> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Update a question by id
    @PutMapping("/update/{id}")
    public QuestionEntity updateQuestion(@PathVariable int id, @RequestBody QuestionEntity updatedQuestion) {
        return questionService.updateQuestion(id, updatedQuestion);
    }

    // Delete a question by id
    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable int id) {
        return questionService.deleteQuestion(id);
    }
}
