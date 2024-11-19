package com.group2.SPEAR_Backend.Repository;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {
    Optional<User> findByEmail (String email);
    List<User> findByIsDeletedFalse();


    @Query("SELECT new com.group2.SPEAR_Backend.Model.User(u.uid, u.firstname, u.lastname, u.email, u.role, u.isDeleted) FROM User u WHERE u.uid = :uid")
    User findByIdWithoutPassword(@Param("uid") int uid);




}
