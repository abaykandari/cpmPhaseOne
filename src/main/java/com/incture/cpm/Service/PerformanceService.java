package com.incture.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Entity.Performance;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.TalentAssessment;
import com.incture.cpm.Repo.EmployeewiseAssignmentRepo;
import com.incture.cpm.Repo.PerformanceRepo;
import com.incture.cpm.Repo.TalentAssessmentRepository;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class PerformanceService {
    @Autowired
    PerformanceRepo performanceRepo;

    @Autowired
    TalentRepository talentRepository;

    @Autowired
    EmployeewiseAssignmentRepo employeewiseAssignmentRepo;

    @Autowired
    TalentAssessmentRepository talentAssessmentRepo;

    public List<Performance> getAllPerformances(){
        return performanceRepo.findAll();
    }

    //to be removed latern on
    public String updatePerformance(Performance performance) {
        Talent existingTalent = talentRepository.findById(performance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Talent not found"));
        
        performanceRepo.findById(performance.getTalentId()).orElseThrow(() -> new IllegalArgumentException("Performance not found"));

        performance.setTalentName(existingTalent.getTalentName());
        performance.setEkYear(existingTalent.getEkYear());
        performance.setTalentSkills(existingTalent.getTalentSkills());
        performanceRepo.save(performance);
        return "Performance created successfully";
    }

    public String addPerformanceWithTalent(Talent talent){
        Performance performance = new Performance();

        performance.setTalentId(talent.getTalentId());
        performance.setTalentName(talent.getTalentName());
        performance.setEkYear(talent.getEkYear());
        performance.setTalentSkills(talent.getTalentSkills());
        performanceRepo.save(performance);

        return "Performance created successfully";
    }

    public String editTalentDetails(Talent talent){
        Performance existingPerformance = performanceRepo.findById(talent.getTalentId()).orElseThrow(() -> new IllegalStateException("Performance not found for given talent"));

        existingPerformance.setTalentName(talent.getTalentName());
        existingPerformance.setEkYear(talent.getEkYear());
        existingPerformance.setTalentSkills(talent.getTalentSkills());
        performanceRepo.save(existingPerformance);
        return "Performance updated successfully";
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

    public String updateAssignmentScore(String email){
        try 
        {
            Talent talent = talentRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Could not find talent"));
            List<EmployeewiseAssignment> assignmentList = employeewiseAssignmentRepo.findByEmployeeEmail(email).orElseThrow(() -> new IllegalArgumentException("No record found for talent"));
            double sum = 0.0;
            int n = 0;
            for (EmployeewiseAssignment assignment : assignmentList) {
                sum += assignment.getEmployeeAssignmentScore();
                n += 1;
            }
            Performance performance = performanceRepo.findById(talent.getTalentId()).get();
            performance.setAssignmentScore(sum/n);
            performanceRepo.save(performance);
            return "Assignment score updated successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    } 

    public String updateAssessmentScore(Long talentId){
        try 
        {
            List<TalentAssessment> talentAssessmentList = talentAssessmentRepo.findAllByTalentId(talentId).orElseThrow(() -> new IllegalArgumentException("Could not find talent"));;
            double sum = 0.0;
            int n = 0;
            for (TalentAssessment talentAssessment : talentAssessmentList) {
                sum += talentAssessment.getScore();
                n += 1;
            }
            Performance performance = performanceRepo.findById(talentId).get();
            performance.setAssessmentScore(sum/n);
            performanceRepo.save(performance); 
            return "Assessment score updated successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }  
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
                updatePerformance(performance);
            } catch (IllegalArgumentException e) {
                return "Error adding performance: " + e.getMessage();
            }
        }
        return "All performances saved successfully";
    }

}
