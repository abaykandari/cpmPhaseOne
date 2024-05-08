package com.example.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cpm.Entity.Attendance;
import com.example.cpm.Entity.Talent;
import com.example.cpm.Repo.AttendanceRepo;
import com.example.cpm.Repo.TalentRepository;

@Service
public class AttendanceService {
    @Autowired
    AttendanceRepo attendanceRepo;

    @Autowired
    TalentRepository talentRepo;

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
}
