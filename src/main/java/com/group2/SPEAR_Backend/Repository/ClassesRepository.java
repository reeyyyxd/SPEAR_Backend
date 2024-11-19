package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    // Find a class by course code
    Classes findByCourseCode(String courseCode);
}
