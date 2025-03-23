package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.QuestionDTO;
import com.group2.SPEAR_Backend.Model.*;
import com.group2.SPEAR_Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository qRepo;

    @Autowired
    private ClassesRepository cRepo;

    @Autowired
    private EvaluationRepository eRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QuestionTemplateRepository templateRepo;

    public QuestionDTO createQuestion(Long classId, Long evaluationId, QuestionDTO questionDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User createdByUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));

        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));

        if (questionDTO.getQuestionType() == null) {
            throw new IllegalArgumentException("QuestionType is required!");
        }

        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        question.setClasses(clazz);
        question.setEvaluation(evaluation);
        question.setQuestionType(questionDTO.getQuestionType());
        question.setCreatedBy(createdByUser);
        question.setReuse(false); // NEW question (not reused)

        Question savedQuestion = qRepo.save(question);
        return mapToDTO(savedQuestion);
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

            if (updatedQuestionDTO.getQuestionType() != null) {
                question.setQuestionType(updatedQuestionDTO.getQuestionType());
            }

            Question savedQuestion = qRepo.save(question);
            return mapToDTO(savedQuestion);
        }).orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));
    }


    public QuestionDTO deleteQuestion(Long questionId) {
        Question question = qRepo.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));

        qRepo.deleteById(questionId);
        return mapToDTO(question);
    }


    public List<QuestionDTO> getQuestionsByCreator(Integer createdByUserId) {
        return qRepo.findByCreatedByUid(createdByUserId).stream()
                .filter(question -> !question.isReuse())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    private QuestionDTO mapToDTO(Question question) {
        return new QuestionDTO(
                question.getQid(),
                question.getQuestionText(),
                question.getEvaluation().getEid(),
                question.getClasses().getCid(),
                question.getQuestionType(),
                question.getCreatedBy().getUid(),
                question.getCreatedBy().getFirstname() + " " + question.getCreatedBy().getLastname(),
                question.isReuse()
        );
    }

    public QuestionDTO useTemplateForClass(Long templateId, Long classId, Long evaluationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with email: " + email));

        QuestionTemplate template = templateRepo.findById(templateId)
                .orElseThrow(() -> new NoSuchElementException("Template not found with ID: " + templateId));

        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));

        Question question = new Question();
        question.setQuestionText(template.getQuestionText());
        question.setQuestionType(template.getQuestionType());
        question.setClasses(clazz);
        question.setEvaluation(evaluation);
        question.setCreatedBy(teacher);
        question.setReuse(true);

        Question savedQuestion = qRepo.save(question);
        return mapToDTO(savedQuestion);
    }


    public List<QuestionDTO> getPreviousQuestionsByCreator() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with email: " + email));

        return qRepo.findByCreatedByUid(teacher.getUid()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public QuestionDTO reusePreviousQuestion(Long questionId, Long classId, Long evaluationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = userRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with email: " + email));

        Question originalQuestion = qRepo.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Original question not found with ID: " + questionId));

        Classes clazz = cRepo.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with ID: " + classId));
        Evaluation evaluation = eRepo.findById(evaluationId)
                .orElseThrow(() -> new NoSuchElementException("Evaluation not found with ID: " + evaluationId));

        Question newQuestion = new Question();
        newQuestion.setQuestionText(originalQuestion.getQuestionText());
        newQuestion.setQuestionType(originalQuestion.getQuestionType());
        newQuestion.setClasses(clazz);
        newQuestion.setEvaluation(evaluation);
        newQuestion.setCreatedBy(teacher);
        newQuestion.setReuse(true);

        Question savedQuestion = qRepo.save(newQuestion);
        return mapToDTO(savedQuestion);
    }








}