package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s WHERE s.teacher.uid = :teacherId")
    List<Schedule> findSchedulesByTeacher(@Param("teacherId") int teacherId);

}
