package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.ClassesEntity;
import com.group2.SPEAR_Backend.Service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
@CrossOrigin(origins = "http://localhost:3000")
public class ClassesController {

    @Autowired
    ClassesService classesService;

    // Create a new class
    @PostMapping("/create")
    public ClassesEntity createClass(@RequestBody ClassesEntity newClass) {
        return classesService.createClass(newClass);
    }

    // Get all classes
    @GetMapping("/all")
    public List<ClassesEntity> getAllClasses() {
        return classesService.getAllClasses();
    }

    // Update a class by id
    @PutMapping("/update/{id}")
    public ClassesEntity updateClass(@PathVariable int id, @RequestBody ClassesEntity updatedClass) {
        return classesService.updateClass(id, updatedClass);
    }

    // Delete a class by id
    @DeleteMapping("/delete/{id}")
    public String deleteClass(@PathVariable int id) {
        return classesService.deleteClass(id);
    }

    // Find a class by name
    @GetMapping("/findByName/{className}")
    public ClassesEntity findClassByName(@PathVariable String className) {
        return classesService.findClassByName(className);
    }
}
