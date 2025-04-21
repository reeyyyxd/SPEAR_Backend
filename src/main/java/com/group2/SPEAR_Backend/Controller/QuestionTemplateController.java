package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.QuestionTemplateDTO;
import com.group2.SPEAR_Backend.DTO.QuestionTemplateSetDTO;
import com.group2.SPEAR_Backend.Service.QuestionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

//    @PostMapping("/teacher/create-set")
//    public QuestionTemplateSetDTO createSet(@RequestParam String name) {
//        return service.createSet(name);
//    }

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

    @GetMapping("/teacher/my-templates")
    public List<QuestionTemplateDTO> getMyTemplates() {
        return service.getTemplatesByCurrentUser();
    }

    @GetMapping("/admin/my-template-sets")
    public List<QuestionTemplateSetDTO> getMyAdminTemplateSets() {
        return service.getTemplateSetsByCurrentUser();
    }

    @DeleteMapping("/admin/delete-set/{setId}")
    public void deleteSet(@PathVariable Long setId) {
        service.deleteSet(setId);
    }

    @DeleteMapping("/teacher/delete-set/{setId}")
    public ResponseEntity<?> deleteSetTeacher(@PathVariable Long setId) {
        try {
            service.deleteSet(setId);
            return ResponseEntity.ok().body(Map.of("message", "Deleted successfully."));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unexpected server error"));
        }
    }


    @DeleteMapping("/admin/delete-question/{questionId}")
    public void deleteQuestion(@PathVariable Long questionId) {
        service.deleteQuestion(questionId);
    }
}
