//package com.group2.SPEAR_Backend.Controller;
//
//import com.group2.SPEAR_Backend.Model.Question;
//import com.group2.SPEAR_Backend.Service.QuestionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/questions")
//@CrossOrigin(origins = "http://localhost:5173")
//public class QuestionController {
//
//    @Autowired
//    QuestionService questionService;
//
//    // Create a new question
//    @PostMapping("/create")
//    public Question createQuestion(@RequestBody Question question) {
//        return questionService.createQuestion(question);
//    }
//
//    // Get all questions
//    @GetMapping("/all")
//    public List<Question> getAllQuestions() {
//        return questionService.getAllQuestions();
//    }
//
//    // Update a question by id
//    @PutMapping("/update/{id}")
//    public Question updateQuestion(@PathVariable int id, @RequestBody Question updatedQuestion) {
//        return questionService.updateQuestion(id, updatedQuestion);
//    }
//
//    // Delete a question by id
//    @DeleteMapping("/delete/{id}")
//    public String deleteQuestion(@PathVariable int id) {
//        return questionService.deleteQuestion(id);
//    }
//}
