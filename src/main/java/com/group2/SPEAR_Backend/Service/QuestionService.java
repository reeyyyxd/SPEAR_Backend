package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.QuestionDTO;
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
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository qRepo;

    @Autowired
    ClassesRepository cRepo;

    @Autowired
    EvaluationRepository eRepo;

    public QuestionDTO createQuestion(Long classId, Long evaluationId, QuestionDTO questionDTO) {
        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));

        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        question.setClasses(clazz);
        question.setEvaluation(evaluation);

        Question savedQuestion = qRepo.save(question);
        return new QuestionDTO(savedQuestion);
    }


    public List<QuestionDTO> getQuestionsByClass(Long classId) {
        return qRepo.findByClassesCid(classId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByEvaluation(Long evaluationId) {
        return qRepo.findByEvaluationEid(evaluationId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestionsByClassAndEvaluation(Long classId, Long evaluationId) {
        return qRepo.findByClassesCidAndEvaluationEid(classId, evaluationId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private QuestionDTO mapToDTO(Question question) {
        return new QuestionDTO(
                question.getQid(),
                question.getQuestionText(),
                question.getEvaluation().getEid(),
                question.getClasses().getCid()
        );
    }

    public QuestionDTO updateQuestion(Long questionId, QuestionDTO updatedQuestionDTO) {
        return qRepo.findById(questionId).map(question -> {
            question.setQuestionText(updatedQuestionDTO.getQuestionText());

            if (updatedQuestionDTO.getEvaluationId() != null) {
                Evaluation evaluation = eRepo.findById(updatedQuestionDTO.getEvaluationId())
                        .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + updatedQuestionDTO.getEvaluationId()));
                question.setEvaluation(evaluation);
            }

            if (updatedQuestionDTO.getClassId() != null) {
                Classes classes = cRepo.findById(updatedQuestionDTO.getClassId())
                        .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + updatedQuestionDTO.getClassId()));
                question.setClasses(classes);
            }

            Question savedQuestion = qRepo.save(question);
            return new QuestionDTO(savedQuestion); // Map back to DTO
        }).orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));
    }


    public QuestionDTO deleteQuestion(Long questionId) {
        Question question = qRepo.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));

        qRepo.deleteById(questionId);
        return new QuestionDTO(question);
    }

}
