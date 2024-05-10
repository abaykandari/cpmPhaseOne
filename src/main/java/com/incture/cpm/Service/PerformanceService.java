package com.incture.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Performance;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.PerformanceRepo;
import com.incture.cpm.Repo.TalentRepository;

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
            throw new IllegalArgumentException("Performance with this ID already exists");

        performance.setTalentName(existingTalent.getTalentName());
        performance.setEkYear(existingTalent.getEkYear());
        performance.setTalentSkills(existingTalent.getTalentSkills());
        performanceRepo.save(performance);
        return "Performance saved successfully";
    }

    public void updateFeedback(Performance performance) {
        Performance existingPerformance = performanceRepo.findById(performance.getTalentId()).orElseThrow(() -> new IllegalStateException("Performance not found for given id"));

        existingPerformance.setAssessmentScore(performance.getAssessmentScore());
        existingPerformance.setAssignmentScore(performance.getAssignmentScore());
        existingPerformance.setAverageAttendance(performance.getAverageAttendance());
        existingPerformance.setPunctuality(performance.getPunctuality());
        existingPerformance.setTechnicalProficiency(performance.getTechnicalProficiency());
        existingPerformance.setProactiveness(performance.getProactiveness());
        existingPerformance.setTimeliness(performance.getTimeliness());
        
        performanceRepo.save(existingPerformance);
    }

    public void deletePerformance(Performance performance) {
        Optional<Performance> existingPerformance = performanceRepo.findById(performance.getTalentId());
        if (existingPerformance.isEmpty()) 
            throw new IllegalArgumentException("Performance with ID " + performance.getTalentId() + " does not exist.");

        performanceRepo.delete(performance);
    }

    public void deletePerformance(Long talentId){
        Optional<Performance> existingPerformance = performanceRepo.findById(talentId);
        if (existingPerformance.isEmpty()) 
            throw new IllegalArgumentException("Performance with ID " + talentId + " does not exist.");
        
        performanceRepo.deleteById(talentId);
    }

    public String addPerformanceByList(List<Performance> performanceList) {
        for (Performance performance : performanceList) {
            try {
                addPerformance(performance);
            } catch (IllegalArgumentException e) {
                return "Error adding performance: " + e.getMessage();
            }
        }
        return "All performances saved successfully";
    }
}
