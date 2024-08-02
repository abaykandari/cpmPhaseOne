package com.incture.cpm.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Exception.BadRequestException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cpm/attendance")
@CrossOrigin("*")
@Tag(name = "Attendance", description = "Endpoints for managing attendance records")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @Operation(summary = "Get All Attendance Records", description = "Retrieve a list of all attendance records. Accessible only to admins.")
    @GetMapping("/getAllAttendance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    @Operation(summary = "Get Attendance by Date", description = "Retrieve attendance records for a specific date.")
    @GetMapping("/getAttendanceByDate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDate(@RequestParam String date) {
        if (date == null || date.isEmpty()) {
            throw new BadRequestException("Date parameter is required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAttendanceByDate(date);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the date: " + date);
        }
    }

    @Operation(summary = "Add Attendance Record", description = "Create a new attendance record.")
    @PostMapping("/addAttendance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addAttendance(@RequestBody Attendance attendance) {
        if (attendance == null) {
            throw new BadRequestException("Attendance data is required.");
        }
        try {
            String message = attendanceService.addAttendance(attendance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add Multiple Attendance Records", description = "Create multiple attendance records at once.")
    @PostMapping("/addAttendanceByList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addAttendanceByList(@RequestBody List<Attendance> attendance) {
        if (attendance == null || attendance.isEmpty()) {
            throw new BadRequestException("Attendance list is required.");
        }
        try {
            String message = attendanceService.addAttendanceByList(attendance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Attendance by Date Range and Talent", description = "Retrieve attendance records for a specific date range and talent ID.")
    @GetMapping("/getAttendanceByDateRangeAndTalent")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDateRangeAndTalent(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam Long talentId) {
        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty() || talentId == null) {
            throw new BadRequestException("Start date, end date, and talent ID are required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAttendanceByDateRangeAndTalent(startDate, endDate, talentId);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the given date range and talent ID: " + talentId);
        }
    }

    @Operation(summary = "Get Attendance by Date and Reporting Manager", description = "Retrieve filtered attendance records by date and reporting manager.")
    @GetMapping("/getAllAttendanceWRTrm")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDateRM(
            @RequestParam String date,
            @RequestParam("rm") String reportingManager) {
        if (date == null || date.isEmpty() || reportingManager == null || reportingManager.isEmpty()) {
            throw new BadRequestException("Date and reporting manager are required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAllAttendanceRM(date, reportingManager);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the date: " + date + " and reporting manager: " + reportingManager);
        }
    }
}
