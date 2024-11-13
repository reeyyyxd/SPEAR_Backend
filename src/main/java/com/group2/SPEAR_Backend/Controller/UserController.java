package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    UserService uServ;


    @PostMapping("/register")
    public User registerUser(@RequestBody User s) {
        return uServ.registerUser(s);
    }

    @GetMapping("/seeAllUsers")
    public List<User> getAll() {
        return uServ.seeAllUser();
    }


    @PutMapping("/updateUser/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User newUser) {
        return uServ.updateUser(id, newUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        return uServ.deleteUser(id);
    }


}