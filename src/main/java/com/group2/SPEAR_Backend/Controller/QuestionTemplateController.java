package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.QuestionTemplateDTO;
import com.group2.SPEAR_Backend.Service.QuestionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173"})
public class QuestionTemplateController {

    @Autowired
    private QuestionTemplateService templateService;

    @PostMapping("/admin/create-questions")
    public QuestionTemplateDTO createTemplate(@RequestBody QuestionTemplateDTO templateDTO) {
        return templateService.createTemplate(templateDTO);
    }

    @GetMapping("/teacher/get-suggested-questions")
    public List<QuestionTemplateDTO> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @GetMapping("/admin/get-suggested-questions")
    public List<QuestionTemplateDTO> getAllTemplatesAdmin() {
        return templateService.getAllTemplates();
    }

    @PutMapping("/admin/update-question/{templateId}")
    public QuestionTemplateDTO updateTemplate(
            @PathVariable Long templateId,
            @RequestBody QuestionTemplateDTO updatedTemplateDTO) {
        return templateService.updateTemplate(templateId, updatedTemplateDTO);
    }

    @DeleteMapping("/admin/delete-question/{templateId}")
    public void deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
    }
}