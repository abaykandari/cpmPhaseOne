package com.incture.cpm.Service;

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

    public List<Attendance> getAllAttendance() {
        return attendanceRepo.findAll();
    }

    public String addAttendance(Attendance attendance) {
        Talent existingTalent = talentRepo.findById(attendance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));
        
        attendance.setTalentName(existingTalent.getTalentName());
        attendance.setEkYear(existingTalent.getEkYear());
        attendance.setTalentCategory(existingTalent.getTalentCategory());
        attendanceRepo.save(attendance);
        return "Attendance saved successfully";
    }

    public String updateRegularize(Long regularizeId) {
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        Attendance originalAttendance = attendanceRepo.findByTalentIdAndDate(regularize.getTalentId(), regularize.getAttendanceDate()).orElseThrow(() -> new IllegalArgumentException("Attendance not found for given data"));

        originalAttendance.setCheckin(regularize.getCheckin());
        originalAttendance.setCheckout(regularize.getCheckout());
        attendanceRepo.save(originalAttendance);
        regularizeRepository.deleteById(regularizeId);

        return "Attendance regularized successfully";
    }

    public Optional<List<Attendance>> getAttendanceByDate(String date) {
        return attendanceRepo.findByDate(date);
    }

    public Optional<List<Attendance>> getAttendanceByDateRangeAndTalent(String startDate,String endDate,Long talentId){
        return attendanceRepo.findByDateRangeAndTalent(startDate,endDate,talentId);
    }

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
}
