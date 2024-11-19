package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    Classes findByCourseCode(String courseCode);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstname, ' ', u.lastname) = :fullName")
    Optional<User> findByFullName(@Param("fullName") String fullName);

    List<Classes> findAllByIsDeletedFalse();

}
