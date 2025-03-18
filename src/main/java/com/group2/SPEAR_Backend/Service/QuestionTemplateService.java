package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Repository.QuestionTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTemplateService {

    @Autowired
    private QuestionTemplateRepository templateRepo;

    public List<QuestionTemplate> getAllTemplates() {
        return templateRepo.findAll();
    }
}