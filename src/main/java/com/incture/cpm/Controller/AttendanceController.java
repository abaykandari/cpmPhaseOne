package com.incture.cpm.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Service.AttendanceService;

@RestController
@RequestMapping("/cpm/attendance")
@CrossOrigin("*")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/getAllAttendance")
    public List<Attendance> getAllAttendance(){
        return attendanceService.getAllAttendance();
    }

    @GetMapping("/getAttendanceByDate")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDate(@RequestParam("date") String date){
        Optional<List<Attendance>> attendanceList =  attendanceService.getAttendanceByDate(date);
        
        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/addAttendance")
    public ResponseEntity<String> addAttendance(@RequestBody Attendance attendance){
        String message = attendanceService.addAttendance(attendance);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/addAttendanceByList")
    public ResponseEntity<String> addAttendanceByList(@RequestBody List<Attendance> attendance){
        String message = attendanceService.addAttendanceByList(attendance);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getAttendanceByDateRangeAndTalent")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDateRangeAndTalent(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("talentId") Long talentId){
        Optional<List<Attendance>> attendanceList = attendanceService.getAttendanceByDateRangeAndTalent(startDate, endDate, talentId);
        
        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
