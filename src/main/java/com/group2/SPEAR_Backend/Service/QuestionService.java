package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import com.group2.SPEAR_Backend.Repository.EvaluationRepository;
import com.group2.SPEAR_Backend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository qRepo;

    @Autowired
    ClassesRepository cRepo;

    @Autowired
    EvaluationRepository eRepo;

    public Question createQuestion(Long classId, Long evaluationId, Question question) {
        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));
        question.setClasses(clazz);
        question.setEvaluation(evaluation);
        return qRepo.save(question);
    }

    public List<Question> getQuestionsByClass(Long classId) {
        return qRepo.findByClassesCid(classId);
    }

    public List<Question> getQuestionsByEvaluation(Long evaluationId) {
        return qRepo.findByEvaluationEid(evaluationId);
    }

    public List<Question> getQuestionsByClassAndEvaluation(Long classId, Long evaluationId) {
        return qRepo.findByClassesCidAndEvaluationEid(classId, evaluationId);
    }

    public Question updateQuestion(Long questionId, Question updatedQuestion) {
        return qRepo.findById(questionId).map(question -> {
            question.setQuestionText(updatedQuestion.getQuestionText());
            question.setEvaluation(updatedQuestion.getEvaluation());
            return qRepo.save(question);
        }).orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));
    }

    public String deleteQuestion(Long questionId) {
        if (qRepo.existsById(questionId)) {
            qRepo.deleteById(questionId);
            return "Question deleted successfully";
        } else {
            throw new NoSuchElementException("Question not found with ID: " + questionId);
        }
    }
}
