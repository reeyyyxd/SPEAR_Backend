package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.QuestionDTO;
import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import com.group2.SPEAR_Backend.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173", "http://10.147.17.166:5173"})
public class QuestionController {

    @Autowired
    private QuestionService qServ;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/teacher/create-question/{classId}/{evaluationId}")
    public QuestionDTO createQuestion(
            @PathVariable Long classId,
            @PathVariable Long evaluationId,
            @RequestBody QuestionDTO questionDTO) {
        return qServ.createQuestion(classId, evaluationId, questionDTO);
    }

    @GetMapping("/teacher/get-my-questions")
    public List<QuestionDTO> getMyQuestions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));

        return qServ.getQuestionsByCreator(teacher.getUid());
    }


    @GetMapping("/get-all-questions-by-class/{classId}")
    public List<QuestionDTO> getQuestionsByClass(@PathVariable Long classId) {
        return qServ.getQuestionsByClass(classId);
    }

    //this is crucial!
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


    @GetMapping("/teacher/get-previous-questions")
    public List<QuestionDTO> getPreviousQuestions() {
        return qServ.getPreviousQuestionsByCreator();
    }

    @PostMapping("/teacher/reuse-question/{questionId}/for-class/{classId}/evaluation/{evaluationId}")
    public QuestionDTO reusePreviousQuestion(
            @PathVariable Long questionId,
            @PathVariable Long classId,
            @PathVariable Long evaluationId) {
        return qServ.reusePreviousQuestion(questionId, classId, evaluationId);
    }

    @PostMapping("/teacher/use-template/{templateId}/for-class/{classId}/evaluation/{evaluationId}")
    public QuestionDTO useTemplateForClass(
            @PathVariable Long templateId,
            @PathVariable Long classId,
            @PathVariable Long evaluationId) {
        return qServ.useTemplateForClass(templateId, classId, evaluationId);
    }

    @PostMapping("/teacher/import-set/{setId}/for-class/{classId}/evaluation/{evaluationId}")
    public List<QuestionDTO> importSet(
            @PathVariable Long setId,
            @PathVariable Long classId,
            @PathVariable Long evaluationId) {
        return qServ.importTemplateSet(setId, classId, evaluationId);
    }



}

