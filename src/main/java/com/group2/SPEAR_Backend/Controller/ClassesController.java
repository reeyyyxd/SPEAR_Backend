package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ClassesDTO;
import com.group2.SPEAR_Backend.Service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/teacher")
public class ClassesController {

    @Autowired
    private ClassesService classesService;

    // Create a new class
    @PostMapping("/create-class")
    public ResponseEntity<ClassesDTO> createClass(@RequestBody ClassesDTO classRequest) {
        ClassesDTO response = classesService.createClass(classRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get all classes
    @GetMapping("/all")
    public ResponseEntity<ClassesDTO> getAllClasses() {
        ClassesDTO response = classesService.getAllClasses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Update a class by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<ClassesDTO> updateClass(@PathVariable Long id, @RequestBody ClassesDTO updatedClass) {
        ClassesDTO response = classesService.updateClass(id, updatedClass);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Delete a class by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClassesDTO> deleteClass(@PathVariable Long id) {
        ClassesDTO response = classesService.deleteClass(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
