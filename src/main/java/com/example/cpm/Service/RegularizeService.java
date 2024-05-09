package com.example.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cpm.Entity.Attendance;
import com.example.cpm.Entity.Regularize;
import com.example.cpm.Repo.AttendanceRepo;
import com.example.cpm.Repo.RegularizeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegularizeService {

    @Autowired
    private RegularizeRepository regularizeRepository;

    @Autowired
    private AttendanceRepo attendanceRepo;

    public List<Regularize> getAllRegularization() {
        return regularizeRepository.findAll();
    }

    public Optional<Regularize> getRegularizeById(int id) {
        return regularizeRepository.findById(id);
    }

    public String createRegularize(Regularize regularize) {
        Attendance attendance = attendanceRepo.findById(regularize.getTalentId()).orElseThrow(() -> new IllegalStateException("Talent not found for the given talentId"));

        regularize.setTalentName(attendance.getTalentName());
        regularizeRepository.save(regularize);
        return "saved";
    }

    public String deleteRegularize(int regularizeId){
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalStateException("Regularize not found for the given id"));

        regularizeRepository.deleteById(regularizeId);
        return "deleted";
    } 
}
