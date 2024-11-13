//package com.group2.SPEAR_Backend.Controller;
//
//import com.group2.SPEAR_Backend.Model.Classes;
//import com.group2.SPEAR_Backend.Service.ClassesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/classes")
//@CrossOrigin(origins = "http://localhost:5173")
//
//
////to be continued
//
//public class ClassesController {
//
//    @Autowired
//    ClassesService classesService;
//
//    // Create a new class
//    @PostMapping("/create")
//    public Classes createClass(@RequestBody Classes newClass) {
//        return classesService.createClass(newClass);
//    }
//
//    // Get all classes
//    @GetMapping("/all")
//    public List<Classes> getAllClasses() {
//        return classesService.getAllClasses();
//    }
//
//    // Update a class by id
//    @PutMapping("/update/{id}")
//    public Classes updateClass(@PathVariable int id, @RequestBody Classes updatedClass) {
//        return classesService.updateClass(id, updatedClass);
//    }
//
//    // Delete a class by id
//    @DeleteMapping("/delete/{id}")
//    public String deleteClass(@PathVariable int id) {
//        return classesService.deleteClass(id);
//    }
//
//    // Find a class by name
//    @GetMapping("/findByName/{className}")
//    public Classes findClassByName(@PathVariable String className) {
//        return classesService.findClassByName(className);
//    }
//}
