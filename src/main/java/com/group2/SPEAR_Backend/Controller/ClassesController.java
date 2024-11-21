package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ClassesDTO;
import com.group2.SPEAR_Backend.Service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ClassesController {


    //remove requestmapping
    // all teacher's control = put /teacher
    // all student's control = put /student
    //TODO: enroll the fucking students


    @Autowired
    private ClassesService classesService;


    @PostMapping("/teacher/createClass")
    public ResponseEntity<ClassesDTO> createClass(@RequestBody ClassesDTO classRequest) {
        ClassesDTO response = classesService.createClass(classRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/teacher/getallclasses")
    public ResponseEntity<ClassesDTO> getAllClasses() {
        ClassesDTO response = classesService.getAllClasses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/teacher/updateClass/{id}")
    public ResponseEntity<ClassesDTO> updateClass(@PathVariable Long id, @RequestBody ClassesDTO updatedClass) {
        ClassesDTO response = classesService.updateClass(id, updatedClass);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/teacher/deleteClass/{id}")
    public ResponseEntity<ClassesDTO> deleteClass(@PathVariable Long id) {
        ClassesDTO response = classesService.deleteClass(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/teacher/getclassKey/{courseCode}")
    public ResponseEntity<ClassesDTO> getClassKeyByCourseCode(@PathVariable String courseCode) {
        ClassesDTO response = classesService.getClassKeyByCourseCode(courseCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/student/enroll")
    public ResponseEntity<ClassesDTO> enrollStudentByClassKey(
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        String classKey = requestBody.get("classKey");
        String email = principal.getName();
        ClassesDTO response = classesService.enrollStudentByClassKey(classKey, email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }





}
