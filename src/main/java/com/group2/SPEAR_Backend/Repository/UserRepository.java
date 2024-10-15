package com.group2.SPEAR_Backend.Repository;
import com.group2.SPEAR_Backend.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository <UserEntity, Integer> {
    UserEntity findByEmail (String email);
}
