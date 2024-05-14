package com.incture.cpm.Service;

import java.time.LocalDate;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Entity.Leaves;
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

    // used temporarily to save single attendance data
    public String addAttendance(Attendance attendance) {
        Talent existingTalent = talentRepo.findById(attendance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));
        
        attendance.setTalentName(existingTalent.getTalentName());
        attendance.setEkYear(existingTalent.getEkYear());
        attendance.setTalentCategory(existingTalent.getTalentCategory());
        attendance.setOfficeLocation(existingTalent.getOfficeLocation());
        attendanceRepo.save(attendance);
        return "Attendance saved successfully";
    }

    // used to approve regularization requests
    public String approveRegularize(Long regularizeId) {
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        Attendance originalAttendance = attendanceRepo.findByTalentIdAndDate(regularize.getTalentId(), regularize.getAttendanceDate()).orElseThrow(() -> new IllegalArgumentException("Attendance not found for given data"));

        originalAttendance.setCheckin(regularize.getCheckin());
        originalAttendance.setCheckout(regularize.getCheckout());
        originalAttendance.setStatus("Present");
        attendanceRepo.save(originalAttendance);
        
        regularize.setApprovalStatus("Approved");
        regularizeRepository.save(regularize);

        return "Attendance regularized successfully";
    }

    public String approveLeave(Leaves leave){
        Talent talent = talentRepo.findById(leave.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));

        List<Attendance> attendanceList = getAttendanceByDateRangeAndTalent(leave.getStartDate(), leave.getEndDate(), talent.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Attendances not found"));
        
        for (Attendance attendance : attendanceList) {
            attendance.setStatus("On Leave");
            attendance.setCheckin(null);
            attendance.setCheckout(null);
        }

        attendanceRepo.saveAll(attendanceList);

        return "Attendance modified successfully";
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