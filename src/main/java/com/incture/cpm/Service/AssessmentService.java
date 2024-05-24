package com.incture.cpm.Service;

import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.AssessmentRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.helper.AssessmentHelp;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentService talentService;

    // isse page par sab talents ki info display hogi
    public List<Talent> getAllTalentsForAssessment() {
        return talentService.getAllTalents();
    }

    // Assessmnet Add krne ke liye
    public String addAssessment(Assessment assessment) {
        Optional<Talent> talent = talentRepository.findById(assessment.getTalentId());

        if (talent.isPresent()) {
            assessmentRepository.save(assessment);
            return "Assessment record saved!";
        }
        return "Talent Id not found";
    }

    // view all assessments for a particular talent
    public List<Assessment> viewAssessmentsForTalent(Long talentId) {
        Optional<List<Assessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            return assessments.get();
        }
        return null;
    }

    // update details of a particular assessment for a particular talent
    public Assessment updateAssessment(Long assessmentId, Long talentId, Assessment updatedAssessment) {
        Optional<List<Assessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            List<Assessment> assessmentList = assessments.get();
            for (Assessment assessment : assessmentList) {
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
        Optional<List<Assessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            List<Assessment> assessmentList = assessments.get();
            for (Assessment assessment : assessmentList) {
                if (assessment.getAssessmentId() == assessmentId && assessment.getTalentId() == talentId) {
                    assessmentRepository.deleteById(assessment.getLocalKey());
                    return true;
                }
            }
        }
        return false;
    }

    public void save(MultipartFile file) {
        try {
            List<Assessment> assessment = AssessmentHelp.convertExcelToAssessmentRecord(file.getInputStream());
            this.assessmentRepository.saveAll(assessment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
