package com.incture.cpm.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Entity.Regularize;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.AttendanceRepo;
import com.incture.cpm.Repo.RegularizeRepository;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepo attendanceRepo;

    @Autowired
    TalentRepository talentRepo;

    @Autowired
    RegularizeRepository regularizeRepository;

    // used to get full attendance list
    public List<Attendance> getAllAttendance() {
        return attendanceRepo.findAll();
    }

    // used to calculate totalHours using checkin and checkout
    private String calculateTotalHours(String checkin, String checkout) {
        // Check if checkin or checkout is null
        if (checkin == null || checkout == null) {
            return ""; // Or handle null values as appropriate
        }
    
        // Parse check-in and check-out times to LocalTime objects
        LocalTime checkinTime = LocalTime.parse(checkin, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime checkoutTime = LocalTime.parse(checkout, DateTimeFormatter.ofPattern("HH:mm:ss"));
    
        // Calculate duration between check-in and check-out times
        Duration duration = Duration.between(checkinTime, checkoutTime);
    
        // Convert duration to hours and minutes
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60); // Remaining minutes
    
        // Format total hours as a string in "HH:mm" format
        return String.format("%02d:%02d", hours, minutes);
    }
    

    // used temporarily to save totalHours field for all attendances
    public String saveHours(){
        List<Attendance> attendanceList = attendanceRepo.findAll();
        
        for (Attendance attendance : attendanceList) {
            String totalHours = calculateTotalHours(attendance.getCheckin(), attendance.getCheckout());
            attendance.setTotalHours(totalHours);
        }
        
        attendanceRepo.saveAll(attendanceList);

        return "Hours saved successfully";
    }

    // used temporarily to save single attendance data
    public String addAttendance(Attendance attendance) {
        Talent existingTalent = talentRepo.findById(attendance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));
        
        attendance.setTalentName(existingTalent.getTalentName());
        attendance.setEkYear(existingTalent.getEkYear());
        attendance.setTalentCategory(existingTalent.getTalentCategory());
        
        String totalHours = calculateTotalHours(attendance.getCheckin(), attendance.getCheckout());
        attendance.setTotalHours(totalHours);

        attendanceRepo.save(attendance);
        return "Attendance saved successfully";
    }

    // used to approve regularization requests
    public String updateRegularize(Long regularizeId) {
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        Attendance originalAttendance = attendanceRepo.findByTalentIdAndDate(regularize.getTalentId(), regularize.getAttendanceDate()).orElseThrow(() -> new IllegalArgumentException("Attendance not found for given data"));

        originalAttendance.setCheckin(regularize.getCheckin());
        originalAttendance.setCheckout(regularize.getCheckout());
        originalAttendance.setStatus("Present");
        String totalHours = calculateTotalHours(originalAttendance.getCheckin(), originalAttendance.getCheckout());
        originalAttendance.setTotalHours(totalHours);
        attendanceRepo.save(originalAttendance);
        regularizeRepository.deleteById(regularizeId);

        return "Attendance regularized successfully";
    }

    // used in daily view
    public Optional<List<Attendance>> getAttendanceByDate(String date) {
        return attendanceRepo.findByDate(date);
    }

    // used in weekly and monthly view
    public Optional<List<Attendance>> getAttendanceByDateRangeAndTalent(String startDate,String endDate,Long talentId){
        return attendanceRepo.findByDateRangeAndTalent(startDate,endDate,talentId);
    }

    // temporarily adding list of attendances
    public String addAttendanceByList(List<Attendance> attendanceList) {
        for (Attendance attendance : attendanceList) {
            try {
                addAttendance(attendance);
            } catch (IllegalArgumentException e) {
                return "Error adding attendance: " + e.getMessage();
            }
        }
        return "All attendances saved successfully";
    }

    //gives filtered attendance data w.r.t to RM
    public Optional<List<Attendance>> getAllAttendanceRM(String date, String reportingManager) {
        List<Talent> talentList = talentRepo.findByReportingManager(reportingManager);
        List<Long> talentIds = new ArrayList<>();
        for (Talent talent : talentList) {
            talentIds.add(talent.getTalentId());
        }
        List<Attendance> attendanceList = new ArrayList<>();
        for (Long talentId : talentIds) {
            Attendance attendance = attendanceRepo.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Attendance not found for talentId: " + talentId));
            attendanceList.add(attendance);
        }
        List<Attendance> ans = new ArrayList<>();
        for(Attendance attendance : attendanceList){
            if(attendance.getDate().equals(date)){
                ans.add(attendance);
            }
        }
        return Optional.of(ans);
    }
    
}
