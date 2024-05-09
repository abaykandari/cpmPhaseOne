package com.example.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cpm.Entity.Performance;
import com.example.cpm.Entity.Talent;
import com.example.cpm.Repo.PerformanceRepo;
import com.example.cpm.Repo.TalentRepository;

@Service
public class PerformanceService {
    @Autowired
    private PerformanceRepo performanceRepo;
    @Autowired
    private TalentRepository talentRepository;

    public List<Performance> getAllPerformances(){
        return performanceRepo.findAll();
    }

    public String addPerformance(Performance performance) {
        Talent existingTalent = talentRepository.findById(performance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));
        
        Optional<Performance> existingPerformance = performanceRepo.findById(performance.getTalentId());
        if (existingPerformance.isPresent()) 
            return "Performance with this ID already exists";

        performance.setTalentName(existingTalent.getTalentName());
        performance.setEkYear(existingTalent.getEkYear());
        performance.setTalentSkills(existingTalent.getTalentSkills());
        performanceRepo.save(performance);
        return "Performance saved successfully";
    }

    public void updateFeedback(Performance performance) {
        Optional<Performance> existingPerformance = performanceRepo.findById(performance.getTalentId());
        if (existingPerformance.isEmpty()) 
            throw new IllegalArgumentException("Performance with ID " + performance.getTalentId() + " does not exist.");

        performanceRepo.save(performance);
    }

    public void deletePerformance(Performance performance) {
        Optional<Performance> existingPerformance = performanceRepo.findById(performance.getTalentId());
        if (existingPerformance.isEmpty()) 
            throw new IllegalArgumentException("Performance with ID " + performance.getTalentId() + " does not exist.");

        performanceRepo.delete(performance);
    }

    public void deletePerformance(int talentId){
        Optional<Performance> existingPerformance = performanceRepo.findById(talentId);
        if (existingPerformance.isEmpty()) 
            throw new IllegalArgumentException("Performance with ID " + talentId + " does not exist.");
        
        performanceRepo.deleteById(talentId);
    }
}
