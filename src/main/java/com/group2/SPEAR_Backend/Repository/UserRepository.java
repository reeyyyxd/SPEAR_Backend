package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.UserDTO;
import com.group2.SPEAR_Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {
    Optional<User> findByEmail (String email);

    List<User> findByIsDeletedFalse();


    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.isDeleted = false")
    List<UserDTO> fetchAllUsersNotDeleted();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'STUDENT' AND u.isDeleted = false")
    List<UserDTO> fetchAllActiveStudents();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'TEACHER' AND u.isDeleted = false")
    List<UserDTO> fetchAllActiveTeachers();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'TEACHER' AND u.isDeleted = true")
    List<UserDTO> fetchAllSoftDeletedTeachers();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'STUDENT' AND u.isDeleted = true")
    List<UserDTO> fetchAllSoftDeletedStudents();
}
