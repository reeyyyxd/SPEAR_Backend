package com.group2.SPEAR_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group2.SPEAR_Backend.Entity.UserEntity;
import com.group2.SPEAR_Backend.Repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    UserRepository spearRepo;

    public UserEntity createUser(UserEntity insert) {
        return spearRepo.save(insert);
    }

    public List<UserEntity> seeAllUser() {
        return spearRepo.findAll();
    }

    @SuppressWarnings("finally")
    public UserEntity updateUser(int uid, UserEntity newUser) {
        UserEntity s = new UserEntity();
        try {
            s = spearRepo.findById(uid).get();
            s.setIdNumber(newUser.getIdNumber());
            s.setFirstname(newUser.getFirstname());
            s.setLastname(newUser.getLastname());
            s.setEmail(newUser.getEmail());
            s.setPassword(newUser.getPassword());

        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException("User " + uid + " not found");
        } finally {
            return spearRepo.save(s);
        }
    }


    public String deleteUser(int uid) {
        if (spearRepo.existsById(uid)) {
            spearRepo.deleteById(uid);
            return "User " + uid + " is deleted";
        } else {
            return "User " + uid + " not found";
        }
    }

    public String validateUserCredentials(String email, String password) {
        UserEntity user = spearRepo.findByEmail(email);

        try {
            if (user != null && user.getPassword().equals(password)) {
                if (user.getRole().equalsIgnoreCase("yes")) {
                    return "Teacher"; // User is a teacher
                } else if (user.getRole().equalsIgnoreCase("yes") && user.getRole().equalsIgnoreCase("yes")) {
                    return "Student";
                } else if (user.getRole().equalsIgnoreCase("yes")) {
                    return "Student"; // User is a student
                } else if (user.getRole() == null && user.getRole() == null) {
                    return "Wait. Admin will verify you.";
                } else {
                    return "Admin"; // User is an admin
                }
            } else {
                return "Invalid email or password";
            }
        } catch (NullPointerException e) {
            return "Wait. Admin will verify you.";
        }
    }
}

