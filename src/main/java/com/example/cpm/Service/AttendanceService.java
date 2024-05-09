package com.example.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cpm.Entity.Attendance;
import com.example.cpm.Entity.Regularize;
import com.example.cpm.Entity.Talent;
import com.example.cpm.Repo.AttendanceRepo;
import com.example.cpm.Repo.RegularizeRepository;
import com.example.cpm.Repo.TalentRepository;

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
        return "saved";
    }
    
    public String regularize(Attendance attendance) {
        Attendance originalAttendance = attendanceRepo.findById(attendance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));

        originalAttendance.setCheckin(attendance.getCheckin());
        originalAttendance.setCheckout(attendance.getCheckout());
        attendanceRepo.save(originalAttendance);  
        return "saved";
    }

    public String updateRegularize(int regularizeId) {
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        Attendance originalAttendance = attendanceRepo.findByTalentIdAndDate(regularize.getTalentId(), regularize.getAttendanceDate()).orElseThrow(() -> new IllegalArgumentException("Attendance not found for given data"));

        originalAttendance.setCheckin(regularize.getCheckin());
        originalAttendance.setCheckout(regularize.getCheckout());
        attendanceRepo.save(originalAttendance);
        regularizeRepository.deleteById(regularizeId);

        return "saved";
    }

    public Optional<List<Attendance>> getAttendanceByDate(String date) {
        return attendanceRepo.findByDate(date);
    }
}
