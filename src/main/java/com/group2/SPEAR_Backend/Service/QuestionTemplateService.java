// --- Service ---
package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.QuestionTemplateDTO;
import com.group2.SPEAR_Backend.DTO.QuestionTemplateSetDTO;
import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Model.QuestionTemplateSet;
import com.group2.SPEAR_Backend.Repository.QuestionTemplateRepository;
import com.group2.SPEAR_Backend.Repository.QuestionTemplateSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class QuestionTemplateService {

    @Autowired
    private QuestionTemplateSetRepository setRepo;

    @Autowired
    private QuestionTemplateRepository templateRepo;

    public QuestionTemplateSetDTO createSet(String name) {
        QuestionTemplateSet set = new QuestionTemplateSet(name);
        return new QuestionTemplateSetDTO(setRepo.save(set));
    }

    public QuestionTemplateDTO addQuestionToSet(Long setId, QuestionTemplateDTO dto) {
        QuestionTemplateSet set = setRepo.findById(setId)
                .orElseThrow(() -> new NoSuchElementException("Set not found with ID: " + setId));

        QuestionTemplate question = new QuestionTemplate(
                dto.getQuestionText(),
                dto.getQuestionType(),
                set
        );

        return new QuestionTemplateDTO(templateRepo.save(question));
    }

    public List<QuestionTemplateSetDTO> getAllSets() {
        return setRepo.findAll().stream().map(QuestionTemplateSetDTO::new).collect(Collectors.toList());
    }

    public QuestionTemplateDTO updateQuestion(Long questionId, QuestionTemplateDTO dto) {
        QuestionTemplate question = templateRepo.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));

        question.setQuestionText(dto.getQuestionText());
        question.setQuestionType(dto.getQuestionType());

        return new QuestionTemplateDTO(templateRepo.save(question));
    }

    public void deleteSet(Long setId) {
        setRepo.deleteById(setId);
    }

    public void deleteQuestion(Long questionId) {
        templateRepo.deleteById(questionId);
    }
}
