package com.group2.SPEAR_Backend.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    public User registerUser (User insert) {
        return userRepo.save(insert);
    }

    public List<User> seeAllUser() {
        return userRepo.findAll();
    }

    public User updateUser(int uid, User newUser) {
        User user = userRepo.findById(uid)
                .orElseThrow(() -> new NoSuchElementException("User " + uid + " not found"));

        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());

        return userRepo.save(user);
    }


    public String deleteUser(int uid) {
        if (userRepo.existsById(uid)) {
            userRepo.deleteById(uid);
            return "User " + uid + " is deleted";
        } else {
            return "User " + uid + " not found";
        }
    }
}

