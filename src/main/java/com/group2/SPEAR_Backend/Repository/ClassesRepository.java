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


    @Query("SELECT c FROM Classes c WHERE c.isDeleted = false AND c.cid = :classId")
    Optional<Classes> findActiveClassById(@Param("classId") Long classId);

    @Query("SELECT c FROM Classes c WHERE c.classKey = :classKey AND c.isDeleted = false")
    Optional<Classes> findByClassKey(@Param("classKey") String classKey);

    @Query("SELECT c FROM Classes c WHERE c.courseCode = :courseCode AND c.section = :section AND c.isDeleted = false")
    Classes findByCourseCodeAndSection(@Param("courseCode") String courseCode, @Param("section") String section);


    @Query("SELECT c.courseName, COUNT(u) + 1 FROM Classes c LEFT JOIN c.enrolledStudents u WHERE c.cid = :classId GROUP BY c.cid")
    Object[] findTotalUsersInClass(@Param("classId") Long classId);






}
