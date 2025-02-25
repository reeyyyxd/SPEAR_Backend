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

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role, u.uid, u.interests, u.department) " +
            "FROM User u WHERE u.role = 'TEACHER' AND u.isDeleted = false")
    List<UserDTO> fetchAllActiveTeachers();


    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'TEACHER' AND u.isDeleted = true")
    List<UserDTO> fetchAllSoftDeletedTeachers();

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE u.role = 'STUDENT' AND u.isDeleted = true")
    List<UserDTO> fetchAllSoftDeletedStudents();


    @Query("SELECT CONCAT(u.firstname, ' ', u.lastname) FROM User u WHERE u.uid = :userId")
    String findFullNameById(@Param("userId") int userId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role) " +
            "FROM User u WHERE (u.firstname = :firstname OR u.lastname = :lastname) AND u.isDeleted = false")
    List<UserDTO> findActiveUsersByName(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(200, 'User retrieved successfully', u.firstname, u.lastname, u.email, u.role, u.uid, u.interests, u.department) " +
            "FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<UserDTO> findByUsernameWithoutPassword(@Param("email") String email);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.UserDTO(u.firstname, u.lastname, u.email, u.interests, u.department) " +
            "FROM User u WHERE u.role = 'TEACHER' AND LOWER(u.department) = LOWER(:department)")
    List<UserDTO> findTeachersByDepartment(@Param("department") String department);





}