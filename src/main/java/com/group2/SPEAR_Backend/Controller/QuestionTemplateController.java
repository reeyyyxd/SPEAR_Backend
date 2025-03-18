package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Service.QuestionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173"})
public class QuestionTemplateController {

    @Autowired
    private QuestionTemplateService templateService;

    @GetMapping("/get-question-templates")
    public List<QuestionTemplate> getAllTemplates() {
        return templateService.getAllTemplates();
    }
}