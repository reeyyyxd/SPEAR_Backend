package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.scheduleOfClasses c WHERE s.teacher.uid = :teacherId")
    List<Schedule> findSchedulesByTeacher(@Param("teacherId") int teacherId);

    @Query("SELECT s FROM Schedule s WHERE CONCAT(s.teacher.firstname, ' ', s.teacher.lastname) = :teacherName AND s.scheduleOfClasses.courseCode = :classCode")
    List<Schedule> findSchedulesByTeacherNameAndClassCode(@Param("teacherName") String teacherName, @Param("classCode") String classCode);

    @Query("SELECT s FROM Schedule s WHERE s.teacher.uid = :teacherId AND NOT EXISTS (SELECT t FROM Team t WHERE t.schedule = s)")
    List<Schedule> findSchedulesByTeacherId(@Param("teacherId") int teacherId);
}
