package com.group2.SPEAR_Backend.Controller;

import
        com.group2.SPEAR_Backend.DTO.ClassesDTO;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Repository.UserRepository;
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





    @Autowired
    private ClassesService cServ;

    @Autowired
    private UserRepository uRepo;


    //all teacher services
    @PostMapping("/teacher/createClass")
    public ResponseEntity<ClassesDTO> createClass(@RequestBody ClassesDTO classRequest) {
        ClassesDTO response = cServ.createClass(classRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

// super nono mka guba og server
//    @GetMapping("/teacher/getallclasses")
//    public ResponseEntity<ClassesDTO> getAllClasses() {
//        ClassesDTO response = cServ.getAllClasses();
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }



    @PutMapping("/teacher/updateClass/{id}")
    public ResponseEntity<ClassesDTO> updateClass(@PathVariable Long id, @RequestBody ClassesDTO updatedClass) {
        ClassesDTO response = cServ.updateClass(id, updatedClass);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/teacher/deleteClass/{id}")
    public ResponseEntity<ClassesDTO> deleteClass(@PathVariable Long id) {
        ClassesDTO response = cServ.deleteClass(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/teacher/getclassKey/{courseCode}/{section}")
    public ResponseEntity<ClassesDTO> getClassKeyByCourseCodeAndSection(
            @PathVariable String courseCode,
            @PathVariable String section) {
        ClassesDTO response = cServ.getClassKeyByCourseCodeAndSection(courseCode, section);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @PostMapping("/student/enroll")
    public ResponseEntity<ClassesDTO> enrollStudentByClassKey(
            @RequestBody Map<String, String> requestBody,
            Principal principal) {
        String classKey = requestBody.get("classKey");
        String email = principal.getName();
        ClassesDTO response = cServ.enrollStudentByClassKey(classKey, email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/class/{classId}/total-users")
    public ResponseEntity<Object[]> getTotalUsersInClass(@PathVariable Long classId) {
        Object[] totalUsers = cServ.getTotalUsersInClass(classId);
        if (totalUsers == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(totalUsers);
    }


    @GetMapping("/teacher/classes-created/{userId}")
    public ResponseEntity<List<ClassesDTO>> getClassesCreatedByUser(@PathVariable int userId) {
        List<ClassesDTO> response = cServ.getClassesCreatedByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/class/{classKey}/students")
    public ResponseEntity<List<UserDTO>> getStudentsInClass(@PathVariable String classKey) {
        List<UserDTO> response = cServ.getStudentsInClass(classKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/enrolled-classes")
    public ResponseEntity<List<ClassesDTO>> getClassesForStudent(@PathVariable int studentId) {
        List<ClassesDTO> response = cServ.getClassesForStudent(studentId);
        return ResponseEntity.ok(response);
    }








}
