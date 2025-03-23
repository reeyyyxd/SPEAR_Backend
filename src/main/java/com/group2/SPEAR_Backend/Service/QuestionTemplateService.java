package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.QuestionTemplateDTO;
import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Repository.QuestionTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class QuestionTemplateService {

    @Autowired
    private QuestionTemplateRepository templateRepo;

    public QuestionTemplateDTO createTemplate(QuestionTemplateDTO templateDTO) {
        QuestionTemplate template = new QuestionTemplate(
                templateDTO.getQuestionText(),
                templateDTO.getQuestionType()
        );

        QuestionTemplate savedTemplate = templateRepo.save(template);
        return new QuestionTemplateDTO(savedTemplate);
    }

    public List<QuestionTemplateDTO> getAllTemplates() {
        return templateRepo.findAll().stream()
                .map(QuestionTemplateDTO::new)
                .collect(Collectors.toList());
    }

    public List<QuestionTemplateDTO> getAllTemplatesAdmin() {
        return templateRepo.findAll().stream()
                .map(QuestionTemplateDTO::new)
                .collect(Collectors.toList());
    }

    public QuestionTemplateDTO updateTemplate(Long templateId, QuestionTemplateDTO updatedTemplateDTO) {
        QuestionTemplate template = templateRepo.findById(templateId)
                .orElseThrow(() -> new NoSuchElementException("Question Template not found with ID: " + templateId));

        template.setQuestionText(updatedTemplateDTO.getQuestionText());
        template.setQuestionType(updatedTemplateDTO.getQuestionType());

        QuestionTemplate savedTemplate = templateRepo.save(template);
        return new QuestionTemplateDTO(savedTemplate);
    }

    public void deleteTemplate(Long templateId) {
        QuestionTemplate template = templateRepo.findById(templateId)
                .orElseThrow(() -> new NoSuchElementException("Question Template not found with ID: " + templateId));

        templateRepo.delete(template);
    }
}