package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.ScheduleDTO;
import com.group2.SPEAR_Backend.Model.DayOfWeek;
import com.group2.SPEAR_Backend.Model.Schedule;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Model.Classes;
import com.group2.SPEAR_Backend.Repository.ScheduleRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import com.group2.SPEAR_Backend.Repository.ClassesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ClassesRepository classesRepo;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        User teacher = userRepo.findById(scheduleDTO.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        Classes scheduleOfClasses = classesRepo.findById(scheduleDTO.getClassId())
                .orElseThrow(() -> new NoSuchElementException("Class not found"));

        Schedule schedule = new Schedule(
                scheduleDTO.getDay(),
                scheduleDTO.getStartTime(),
                scheduleDTO.getEndTime(),
                teacher,
                scheduleOfClasses
        );
        Schedule savedSchedule = scheduleRepo.save(schedule);
        return ScheduleDTO.convertToDTO(savedSchedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ScheduleDTO> getSchedulesByTeacher(int teacherId) {
        List<Schedule> schedules = scheduleRepo.findSchedulesByTeacher(teacherId);
        if (schedules.isEmpty()) {
            throw new NoSuchElementException("No schedules found");
        }
        return schedules.stream().map(this::convertToDTO).toList();
    }

    public List<ScheduleDTO> getSchedulesByTeacherNameAndClassCode(String teacherName, String classCode) {
        List<Schedule> schedules = scheduleRepo.findSchedulesByTeacherNameAndClassCode(teacherName, classCode);
        if (schedules.isEmpty()) {
            throw new NoSuchElementException("No schedules found for teacher " + teacherName + " and class code " + classCode);
        }
        return schedules.stream().map(this::convertToDTO).toList();
    }

    public ScheduleDTO updateSchedule(int schedid, ScheduleDTO updatedScheduleDTO) {
        Schedule existingSchedule = scheduleRepo.findById(schedid)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + schedid + " not found"));

        User teacher = userRepo.findById(updatedScheduleDTO.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        Classes scheduleOfClasses = classesRepo.findById(updatedScheduleDTO.getClassId())
                .orElseThrow(() -> new NoSuchElementException("Class not found"));

        existingSchedule.setDay(updatedScheduleDTO.getDay());
        existingSchedule.setStartTime(updatedScheduleDTO.getStartTime());
        existingSchedule.setEndTime(updatedScheduleDTO.getEndTime());
        existingSchedule.setTeacher(teacher);
        existingSchedule.setScheduleOfClasses(scheduleOfClasses);

        Schedule updatedSchedule = scheduleRepo.save(existingSchedule);
        return ScheduleDTO.convertToDTO(updatedSchedule);
    }

    public void deleteSchedule(int schedid) {
        Schedule schedule = scheduleRepo.findById(schedid)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + schedid + " not found"));
        scheduleRepo.delete(schedule);
    }

    public List<ScheduleDTO> getSchedulesForClassesNeedingAdvisers() {
        return scheduleRepo.findAll().stream()
                .filter(schedule -> schedule.getScheduleOfClasses().isNeedsAdvisory())
                .map(this::convertToDTO)
                .toList();
    }

    private ScheduleDTO convertToDTO(Schedule schedule) {
        return new ScheduleDTO(
                schedule.getSchedid(),
                schedule.getDay(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getTeacher().getUid(),
                schedule.getTeacher().getFirstname() + " " + schedule.getTeacher().getLastname(),
                schedule.getScheduleOfClasses().getCid(),
                schedule.getScheduleOfClasses().getCourseCode(),
                schedule.getScheduleOfClasses().getCourseDescription()
        );
    }
}