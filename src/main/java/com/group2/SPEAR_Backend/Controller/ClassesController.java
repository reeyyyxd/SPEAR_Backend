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


    @PostMapping("/createClass")
    public ResponseEntity<ClassesDTO> createClass(@RequestBody ClassesDTO classRequest) {
        ClassesDTO response = classesService.createClass(classRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/getallclasses")
    public ResponseEntity<ClassesDTO> getAllClasses() {
        ClassesDTO response = classesService.getAllClasses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/updateClass/{id}")
    public ResponseEntity<ClassesDTO> updateClass(@PathVariable Long id, @RequestBody ClassesDTO updatedClass) {
        ClassesDTO response = classesService.updateClass(id, updatedClass);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/deleteClass/{id}")
    public ResponseEntity<ClassesDTO> deleteClass(@PathVariable Long id) {
        ClassesDTO response = classesService.deleteClass(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getclassKey/{courseCode}")
    public ResponseEntity<ClassesDTO> getClassKeyByCourseCode(@PathVariable String courseCode) {
        ClassesDTO response = classesService.getClassKeyByCourseCode(courseCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
