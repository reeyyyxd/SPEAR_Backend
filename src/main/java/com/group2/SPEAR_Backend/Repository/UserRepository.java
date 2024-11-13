package com.group2.SPEAR_Backend.Repository;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail (String email);
}
