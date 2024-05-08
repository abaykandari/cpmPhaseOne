package com.example.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cpm.Entity.Attendance;
import com.example.cpm.Service.AttendanceService;

@RestController
@RequestMapping("/cpm/attendance")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/getAllAttendance")
    public List<Attendance> getAllAttendance(){
        return attendanceService.getAllAttendance();
    }

    @PostMapping("/addAttendance")
    public ResponseEntity<String> addAttendance(@RequestBody Attendance attendance){
        String message = attendanceService.addAttendance(attendance);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/regularize")
    public ResponseEntity<String> regularize(@RequestBody Attendance attendance){
        attendanceService.regularize(attendance);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
