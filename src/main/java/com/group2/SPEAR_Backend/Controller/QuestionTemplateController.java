package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.QuestionTemplateDTO;
import com.group2.SPEAR_Backend.DTO.QuestionTemplateSetDTO;
import com.group2.SPEAR_Backend.Service.QuestionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/templates")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.147.17.37:5173", "http://10.147.17.166:5173"})
public class QuestionTemplateController {

    @Autowired
    private QuestionTemplateService service;

    @PostMapping("/admin/create-set")
    public QuestionTemplateSetDTO createSet(@RequestParam String name) {
        return service.createSet(name);
    }

    @PostMapping("/admin/add-question/{setId}")
    public QuestionTemplateDTO addQuestionToSet(@PathVariable Long setId, @RequestBody QuestionTemplateDTO dto) {
        return service.addQuestionToSet(setId, dto);
    }

    @PutMapping("/admin/update-question/{questionId}")
    public QuestionTemplateDTO updateQuestion(@PathVariable Long questionId, @RequestBody QuestionTemplateDTO dto) {
        return service.updateQuestion(questionId, dto);
    }

    @GetMapping("/teacher/get-template-sets")
    public List<QuestionTemplateSetDTO> getAllSets() {
        return service.getAllSets();
    }

//    @GetMapping("/teacher/my-templates")
//    public List<QuestionTemplateDTO> getMyTemplates() {
//        return service.getTemplatesByCurrentUser();
//    }

    @DeleteMapping("/admin/delete-set/{setId}")
    public void deleteSet(@PathVariable Long setId) {
        service.deleteSet(setId);
    }

    @DeleteMapping("/admin/delete-question/{questionId}")
    public void deleteQuestion(@PathVariable Long questionId) {
        service.deleteQuestion(questionId);
    }
}
