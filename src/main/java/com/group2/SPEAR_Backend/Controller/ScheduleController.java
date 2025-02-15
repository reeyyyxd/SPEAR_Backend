package com.group2.SPEAR_Backend.Controller;

import com.group2.SPEAR_Backend.DTO.ScheduleDTO;
import com.group2.SPEAR_Backend.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/teacher/create-schedule")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleDTO));
    }

    @GetMapping("/getallschedules")
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

//    @GetMapping("/teacher/{teacherId}")
//    public ResponseEntity<List<ScheduleDTO>> getSchedulesByTeacher(@PathVariable int teacherId) {
//        return ResponseEntity.ok(scheduleService.getSchedulesByTeacher(teacherId));
//    }

    @PutMapping("/update-schedule/{schedid}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable int schedid,
            @RequestBody ScheduleDTO updatedScheduleDTO
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(schedid, updatedScheduleDTO));
    }

    @DeleteMapping("/delete-schedule/{schedid}")
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable int schedid) {
        scheduleService.deleteSchedule(schedid);
        return ResponseEntity.ok(Map.of("message", "Schedule deleted successfully"));
    }
}
