package com.group2.SPEAR_Backend.Service;

import com.group2.SPEAR_Backend.DTO.ScheduleDTO;
import com.group2.SPEAR_Backend.Model.Schedule;
import com.group2.SPEAR_Backend.Model.User;
import com.group2.SPEAR_Backend.Repository.ScheduleRepository;
import com.group2.SPEAR_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private UserRepository userRepo;

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        User teacher = userRepo.findById(scheduleDTO.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        Schedule schedule = new Schedule(scheduleDTO.getDay(), scheduleDTO.getTime(), teacher);
        Schedule savedSchedule = scheduleRepo.save(schedule);

        return new ScheduleDTO(
                savedSchedule.getSchedid(),
                savedSchedule.getDay(),
                savedSchedule.getTime(),
                savedSchedule.getTeacher().getUid(),
                savedSchedule.getTeacher().getFirstname() + " " + savedSchedule.getTeacher().getLastname()
        );
    }

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepo.findAll().stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getSchedid(),
                        schedule.getDay(),
                        schedule.getTime(),
                        schedule.getTeacher().getUid(),
                        schedule.getTeacher().getFirstname() + " " + schedule.getTeacher().getLastname()
                ))
                .toList();
    }

//    public List<ScheduleDTO> getSchedulesByTeacher(int teacherId) {
//        List<Schedule> schedules = scheduleRepo.findSchedulesByTeacher(teacherId);
//        if (schedules.isEmpty()) {
//            throw new NoSuchElementException("No schedules found for teacher ID " + teacherId);
//        }
//
//        return schedules.stream()
//                .map(schedule -> new ScheduleDTO(
//                        schedule.getSchedid(),
//                        schedule.getDay(),
//                        schedule.getTime(),
//                        schedule.getTeacher().getUid(),
//                        schedule.getTeacher().getFirstname() + " " + schedule.getTeacher().getLastname()
//                ))
//                .toList();
//    }

    public ScheduleDTO updateSchedule(int schedid, ScheduleDTO updatedScheduleDTO) {
        Schedule existingSchedule = scheduleRepo.findById(schedid)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + schedid + " not found"));

        User teacher = userRepo.findById(updatedScheduleDTO.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("Teacher not found"));

        existingSchedule.setDay(updatedScheduleDTO.getDay());
        existingSchedule.setTime(updatedScheduleDTO.getTime());
        existingSchedule.setTeacher(teacher);

        Schedule updatedSchedule = scheduleRepo.save(existingSchedule);

        return new ScheduleDTO(
                updatedSchedule.getSchedid(),
                updatedSchedule.getDay(),
                updatedSchedule.getTime(),
                updatedSchedule.getTeacher().getUid(),
                updatedSchedule.getTeacher().getFirstname() + " " + updatedSchedule.getTeacher().getLastname()
        );
    }

    public void deleteSchedule(int schedid) {
        Schedule schedule = scheduleRepo.findById(schedid)
                .orElseThrow(() -> new NoSuchElementException("Schedule with ID " + schedid + " not found"));
        scheduleRepo.delete(schedule);
    }
}
