package com.incture.cpm.Service;

import com.incture.cpm.Entity.TalentAssessment;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.TalentAssessmentRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.helper.AssessmentHelp;


import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;

@Service
public class TalentAssessmentService {

    @Autowired
    private TalentAssessmentRepository assessmentRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentService talentService;

    // isse page par sab talents ki info display hogi
    public List<Talent> getAllTalentsForAssessment() {
        return talentService.getAllTalents();
    }

    // Assessmnet Add krne ke liye
    public String addAssessment(TalentAssessment assessment) {
        Optional<Talent> talent = talentRepository.findById(assessment.getTalentId());

        if (talent.isPresent()) {
            assessmentRepository.save(assessment);
            return "Assessment record saved!";
        }
        return "Talent Id not found";
    }

    // view all assessments for a particular talent
    public List<TalentAssessment> viewAssessmentsForTalent(Long talentId) {
        Optional<List<TalentAssessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            return assessments.get();
        }
        return null;
    }

    // update details of a particular assessment for a particular talent
    public TalentAssessment updateAssessment(Long assessmentId, Long talentId, TalentAssessment updatedAssessment) {
        Optional<List<TalentAssessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            List<TalentAssessment> assessmentList = assessments.get();
            for (TalentAssessment assessment : assessmentList) {
                if (assessment.getAssessmentId() == assessmentId && assessment.getTalentId() == talentId) {
                    updatedAssessment.setLocalKey(assessment.getLocalKey());
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);
                    updatedAssessment.setComments(formattedDateTime+" -> "+updatedAssessment.getComments() + "\n" + assessment.getComments());

                    return assessmentRepository.save(updatedAssessment);
                }
            }
        }
        return null;
    }

    // delete details of a particular assessment for a particular talent
    public boolean deleteAssessment(Long assessmentId, Long talentId) {
        Optional<List<TalentAssessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            List<TalentAssessment> assessmentList = assessments.get();
            for (TalentAssessment assessment : assessmentList) {
                if (assessment.getAssessmentId() == assessmentId && assessment.getTalentId() == talentId) {
                    assessmentRepository.deleteById(assessment.getLocalKey());
                    return true;
                }
            }
        }
        return false;
    }

    public String save(MultipartFile file) {
        try {
            List<TalentAssessment> assessmentList = AssessmentHelp
                    .convertExcelToAssessmentRecord(file.getInputStream());
           
                String message=saveIndividually(assessmentList);
                return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some error occurred: ";
    }

    @Transactional
    public String saveIndividually(List<TalentAssessment> assessmentList) {
        try {
             for (TalentAssessment assessment : assessmentList) {
            Optional<TalentAssessment> assessmentRepoFetch = assessmentRepository
                    .findByAssessmentIdAndTalentId(assessment.getAssessmentId(), assessment.getTalentId());
            if (assessmentRepoFetch.isPresent()) {
                TalentAssessment existingAssessment = assessmentRepoFetch.get();
                assessment.setLocalKey(existingAssessment.getLocalKey());
                assessment.setAttempts(existingAssessment.getAttempts() + 1);
                assessmentRepository.save(assessment);
            } else {
                assessment.setAttempts(1);
                assessmentRepository.save(assessment);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            return "Some error occurred: " + e.getMessage();
        }
        return "Data Saved Successfully";
    }

    public List<TalentAssessment> getUniqueAssessments() {
        List<TalentAssessment> allAssessments = assessmentRepository.findAll();
        Map<Long, TalentAssessment> uniqueAssessmentsMap = new HashMap<>();
        for (TalentAssessment assessment : allAssessments) {
            uniqueAssessmentsMap.putIfAbsent(assessment.getAssessmentId(), assessment);
        }
        return List.copyOf(uniqueAssessmentsMap.values());
    }


    public List<TalentAssessment> getAllAssessmentsByAssessmentId(Long assessmentId) {
        return assessmentRepository.findAllByAssessmentId(assessmentId);
    }

}
