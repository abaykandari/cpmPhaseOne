package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Entity.Regularize;
import com.incture.cpm.Repo.AttendanceRepo;
import com.incture.cpm.Repo.RegularizeRepository;

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

    public List<Regularize> getAllPendingRegularization() {
        return regularizeRepository.findByApprovalStatus("Pending");
    }
    
    public Optional<Regularize> getRegularizeById(Long regularizeId) {
        return regularizeRepository.findById(regularizeId);
    }

    public String createRegularize(Regularize regularize) {
        Attendance attendance = attendanceRepo.findById(regularize.getTalentId()).orElseThrow(() -> new IllegalStateException("Talent not found for the given talentId"));

        regularize.setTalentName(attendance.getTalentName());
        regularize.setApprovalStatus("Pending");
        regularizeRepository.save(regularize);
        return "saved";
    }

    public String deleteRegularize(Long regularizeId){
        @SuppressWarnings("unused")
        Regularize regularize = regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalStateException("Regularize not found for the given id"));

        regularizeRepository.deleteById(regularizeId);
        return "deleted";
    }

    public String declineRegularize(Long id) {
        Regularize regularize = regularizeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        regularize.setApprovalStatus("Declined");
        regularizeRepository.save(regularize);

        return "Regularization successfully declined"; 
    } 
    
    public String createRegularizeList(List<Regularize> regularizeList) {
        for (Regularize regularize : regularizeList) {
            try {
                createRegularize(regularize);
            } catch (IllegalArgumentException e) {
                return "Error adding regularization: " + e.getMessage();
            }
        }
        return "All regularizations saved successfully";
    }
}
