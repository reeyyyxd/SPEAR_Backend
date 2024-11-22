package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService uServ;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO reg){
        return ResponseEntity.ok(uServ.register(reg));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO req){
        return ResponseEntity.ok(uServ.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refreshToken(@RequestBody UserDTO req){
        return ResponseEntity.ok(uServ.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUsers() {
        return ResponseEntity.ok(uServ.getAllUsers());
    }


    @GetMapping("/admin/getUsers/{userId}")
    public ResponseEntity<UserDTO> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(uServ.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer userId, @RequestBody User reqres){
        return ResponseEntity.ok(uServ.updateUser(userId, reqres));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<UserDTO> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDTO response = uServ.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(uServ.deleteUser(userId));
    }

    @GetMapping("/admin/users/active")
    public ResponseEntity<List<UserDTO>> getAllActiveUsers() {
        return ResponseEntity.ok(uServ.getAllActiveUsers());
    }

    @GetMapping("/admin/users/active-students")
    public ResponseEntity<List<UserDTO>> getAllActiveStudents() {
        return ResponseEntity.ok(uServ.getAllActiveStudents());
    }

    @GetMapping("/admin/users/active-teachers")
    public ResponseEntity<List<UserDTO>> getAllActiveTeachers() {
        return ResponseEntity.ok(uServ.getAllActiveTeachers());
    }

    @GetMapping("/admin/users/deleted-teachers")
    public ResponseEntity<List<UserDTO>> getAllSoftDeletedTeachers() {
        return ResponseEntity.ok(uServ.getAllSoftDeletedTeachers());
    }

    @GetMapping("/admin/users/deleted-students")
    public ResponseEntity<List<UserDTO>> getAllSoftDeletedStudents() {
        return ResponseEntity.ok(uServ.getAllSoftDeletedStudents());
    }



}