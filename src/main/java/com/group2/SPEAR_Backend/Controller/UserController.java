package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Entity.UserEntity;
import com.group2.SPEAR_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService spearServ;


    @PostMapping("/createUser")
    public UserEntity createUser(@RequestBody UserEntity s) {
        return spearServ.createUser(s);
    }

    @GetMapping("/seeAllUsers")
    public List<UserEntity> getAll() {
        return spearServ.seeAllUser();
    }


    @PutMapping("/updateUser/{id}")
    public UserEntity updateUser(@PathVariable int id, @RequestBody UserEntity newUser) {
        return spearServ.updateUser(id, newUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        return spearServ.deleteUser(id);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserEntity loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        String userType = spearServ.validateUserCredentials(email, password);

        if (!userType.equals("Invalid email or password")) {
            return "Login successful as " + userType;
        } else {
            return "Invalid email or password";
        }
    }
}