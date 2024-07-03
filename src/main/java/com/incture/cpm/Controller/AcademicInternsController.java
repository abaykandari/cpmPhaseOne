package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.AcademicInterns;
import com.incture.cpm.Service.AcademicInternsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cpm/api/attendance")
@CrossOrigin("*")
public class AcademicInternsController {

    @Autowired
    private AcademicInternsService academicInternsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                List<String[]> csvData = readCsvData(file.getInputStream());
                academicInternsService.saveAttendanceFromCsv(csvData);
                return ResponseEntity.ok("CSV file uploaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CSV file");
            }
        } else {
            return ResponseEntity.badRequest().body("Empty file uploaded");
        }
    }

    private List<String[]> readCsvData(java.io.InputStream inputStream) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                csvData.add(line.split(","));
            }
        }
        return csvData;
    }

    @GetMapping
    public ResponseEntity<List<AcademicInterns>> getAttendance() {
        return ResponseEntity.ok(academicInternsService.getAllAttendanceRecords());
    }

    // used in daily view
    @GetMapping("/getAttendanceByDate")
    public ResponseEntity<Optional<List<AcademicInterns>>> getAttendanceByDate(@RequestParam("date") String date) {
        Optional<List<AcademicInterns>> attendanceList = academicInternsService.getAttendanceByDateAndStatus(date, "Present");

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/updateAttendanceById/{id}")
    public ResponseEntity<AcademicInterns> updateAttendance(@PathVariable("id") Long id,
            @RequestBody AcademicInterns updatedAttendance) {
        Optional<AcademicInterns> existingAttendanceOptional = academicInternsService.getAttendanceById(id);
        if (existingAttendanceOptional.isPresent()) {
            AcademicInterns updatedRecord = academicInternsService.updateAttendance(id, updatedAttendance);
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteAttendance/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable("id") Long id) {
        Optional<AcademicInterns> existingAttendanceOptional = academicInternsService.getAttendanceById(id);
        if (existingAttendanceOptional.isPresent()) {
            academicInternsService.deleteAttendance(existingAttendanceOptional.get());
            return ResponseEntity.ok("Attendance record deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}