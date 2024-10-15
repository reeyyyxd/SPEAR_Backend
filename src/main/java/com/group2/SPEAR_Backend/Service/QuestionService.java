package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Entity.QuestionEntity;
import com.group2.SPEAR_Backend.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    // Create a new question
    public QuestionEntity createQuestion(QuestionEntity question) {
        return questionRepository.save(question);
    }

    // Get all questions
    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    // Update a question
    public QuestionEntity updateQuestion(int id, QuestionEntity updatedQuestion) {
        try {
            QuestionEntity existingQuestion = questionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Question with id " + id + " not found"));

            existingQuestion.setQuestion(updatedQuestion.getQuestion());
            existingQuestion.setAnswer(updatedQuestion.getAnswer());
            existingQuestion.setIdEvaluator(updatedQuestion.getIdEvaluator());

            return questionRepository.save(existingQuestion);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Question with id " + id + " not found");
        }
    }

    // Delete a question
    public String deleteQuestion(int id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return "Question with id " + id + " deleted";
        } else {
            return "Question with id " + id + " not found";
        }
    }
}
