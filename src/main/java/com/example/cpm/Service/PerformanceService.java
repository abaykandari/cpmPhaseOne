package com.example.cpm.Service;

import java.util.List;

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

        performance.setTalentName(existingTalent.getTalentName());
        performance.setEkYear(existingTalent.getEkYear());
        performance.setTalentSkills(existingTalent.getTalentSkills());
        performanceRepo.save(performance);
        return "saved";
    }

    public void updateFeedback(Performance performance) {
        performanceRepo.save(performance);
    }

    public void deletePerformance(Performance performance) {
        performanceRepo.delete(performance);
    }
}
