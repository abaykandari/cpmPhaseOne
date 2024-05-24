package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incture.cpm.Entity.AcademicInterns;
import com.incture.cpm.Repo.AcademicInternsRepository;
 
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Service
public class AcademicInternsService {
    @Autowired
    private AcademicInternsRepository academicInternsRepository;
 
    public void saveAttendanceFromCsv(List<String[]> csvData) {
        Map<String, AcademicInterns> attendanceMap = csvData.stream()
                .skip(1) // Skip the header row
                .map(this::mapRowToAttendance)
                .collect(Collectors.toMap(
                        AcademicInterns::getEmail,
                        attendance -> attendance,
                        this::mergeAttendanceRecords));
 
        List<AcademicInterns> attendanceList = attendanceMap.values().stream()
                .collect(Collectors.toList());
 
        // Calculate the total meeting duration in minutes
        long totalMeetingDurationMinutes = attendanceList.stream()
                .mapToLong(
                        AcademicInterns::getDurationMinutes)
                .max()
                .orElse(0);
 
        // Set attendance status based on the individual's duration in minutes
        attendanceList.forEach(attendance -> {
            long individualDurationMinutes = attendance.getDurationMinutes();
            if (individualDurationMinutes <= (totalMeetingDurationMinutes * 0.15)) {
                attendance.setStatus("Absent");
            } else {
                attendance.setStatus("Present");
            }
        });
 
        academicInternsRepository.saveAll(attendanceList);
        // saveAll(attendanceList);
    }
 
    private AcademicInterns mapRowToAttendance(String[] row) {
        AcademicInterns attendance = new AcademicInterns();
        attendance.setFullName(row[0]);
        attendance.setDate(formatDate(row[1].substring(1)));
        attendance.setJoinTime(row[2].substring(0, row[2].length()-1));
        attendance.setLeaveTime(row[4].substring(0, row[4].length()-1));
        attendance.setDuration(parseDuration(row[5]));
        attendance.setDurationMinutes(getDurationInMinutes(attendance.getDuration()));
        attendance.setEmail(row[6]);
        attendance.setRole(row[7]);
        attendance.setParticipantId(row[8]);
        return attendance;
    }
 
    private String formatDate(String date) {
        String[] dateParts = date.split("/");
        if (dateParts.length == 3) {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);
   
            // System.out.println(String.format("%02d/%02d/%04d", day, month, year));
            return String.format("%02d/%02d/%04d", day, month, year);
           
        }
       
        return date;
    }
 
 
 
    private long getDurationInMinutes(Duration duration) {
        return duration.toMinutes();
    }
 
    private Duration parseDuration(String durationString) {
        try {
            String[] parts = durationString.split("m");
            long minutes = Long.parseLong(parts[0].trim());
            long seconds = 0;
            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                seconds = Long.parseLong(parts[1].trim().replace("s", ""));
            }
            return Duration.ofMinutes(minutes).plusSeconds(seconds);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Handle invalid duration format
            return Duration.ZERO;
        }
    }
 
    private AcademicInterns mergeAttendanceRecords(AcademicInterns existing, AcademicInterns newAttendance) {
        existing.setDuration(existing.getDuration().plus(newAttendance.getDuration()));
        existing.setDurationMinutes(getDurationInMinutes(existing.getDuration()));
        return existing;
    }
 
    public List<AcademicInterns> getAllAttendanceRecords() {
        return academicInternsRepository.findAll();
    }
 
    public Optional<List<AcademicInterns>> getAttendanceByDateAndStatus(String date, String status) {
        return academicInternsRepository.findByDateAndStatus(date, status);
    }
 
    public void deleteAttendance(AcademicInterns attendance) {
        academicInternsRepository.delete(attendance);
    }
 
    public AcademicInterns updateAttendance(Long id, AcademicInterns attendance) {
        attendance.setId(id);
        return academicInternsRepository.save(attendance);
    }
 
    public Optional<AcademicInterns> getAttendanceById(Long id) {
        return academicInternsRepository.findById(id);
    }
}