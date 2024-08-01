package com.incture.cpm.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.AssessmentLevelFive;
import com.incture.cpm.Entity.AssessmentLevelFour;
import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.AssessmentLevelFinal;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Repo.AssessmentRepo;
import com.incture.cpm.Repo.CandidateRepository;
import com.incture.cpm.Repo.CollegeRepository;
import com.incture.cpm.Util.ExcelUtil;

@Service
public class AssessmentService {
    @Autowired
    AssessmentRepo assessmentRepo;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    TalentService talentService;

    @Autowired
    CollegeRepository collegeRepository;

    @Autowired
    ExcelUtil excelUtil;

    public List<Assessment> getAllAssessments() {
        return assessmentRepo.findAll();
    }

    public List<Assessment> getAssessmentByCollegeId(int collegeId) {
        return assessmentRepo.findAllByCollege(collegeRepository.findById(collegeId).
                                orElseThrow(() -> new IllegalArgumentException("No College Found For id :" + collegeId)))
                                .orElseThrow(() -> new IllegalArgumentException("Assessment for given college not found"));
    }

    public String loadCandidates(int collegeId){
        List<Candidate> candidates = candidateRepository.findByCollegeId(collegeId);
        
        for(Candidate candidate : candidates){
            String email = candidate.getEmail();
            Assessment existingAssessment = assessmentRepo.findByEmail(email).orElse(null);
            if(existingAssessment == null){
                Assessment assessment = new Assessment();
                assessment.setEmail(email);
                assessment.setCandidateName(candidate.getCandidateName());
                assessment.setCollege(collegeRepository.findById(collegeId).orElseThrow(() -> new IllegalArgumentException("College not found college id: " + collegeId)));
                assessmentRepo.save(assessment);
            }
        }

        CollegeTPO college = collegeRepository.findById(collegeId).orElseThrow(() -> new IllegalArgumentException("College not found for id: " +collegeId));
        college.setAssessmentStatus("Started");
        collegeRepository.save(college); 
        return "Candidates Loaded Successfully";
    }

    public String updateLevelOne(AssessmentLevelOne assessmentLevelOne) {
        Assessment assessment = assessmentRepo.findByEmail(assessmentLevelOne.getEmail()).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the provided email address"));
        assessment.setAssessmentLevelOne(assessmentLevelOne);
        assessmentLevelOne.updateTotalScore();
        assessment.updateTotalScore();
        
        assessmentRepo.save(assessment);
        return "Assessment Level One updated successfully";
    }

    public String updateLevelTwo(AssessmentLevelTwo assessmentLevelTwo) {
        Assessment assessment = assessmentRepo.findByEmail(assessmentLevelTwo.getEmail()).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the provided email address"));
        assessment.setAssessmentLevelTwo(assessmentLevelTwo);
        assessmentLevelTwo.updateTotalScore();
        assessment.updateTotalScore();
        assessmentRepo.save(assessment);
        return "Assessment Level Two updated successfully";
    }

    public String updateLevelThree(AssessmentLevelThree assessmentLevelThree) {
        Assessment assessment = assessmentRepo.findByEmail(assessmentLevelThree.getEmail()).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the provided email address"));
        assessment.setAssessmentLevelThree(assessmentLevelThree);
        assessmentLevelThree.updateTotalScore();
        assessment.updateTotalScore();
        assessmentRepo.save(assessment);
        return "Assessment Level Three updated successfully";
    }

    public String updateLevelFour(AssessmentLevelFour assessmentLevelFour) {
        Assessment assessment = assessmentRepo.findByEmail(assessmentLevelFour.getEmail()).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the provided email address"));
        assessment.setAssessmentLevelFour(assessmentLevelFour);
        assessmentLevelFour.updateTotalScore();
        assessment.updateTotalScore();
        assessmentRepo.save(assessment);
        return "Assessment Level Four updated successfully";
    }

    public String updateLevelFive(AssessmentLevelFive assessmentLevelFive) {
        Assessment assessment = assessmentRepo.findByEmail(assessmentLevelFive.getEmail()).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the provided email address"));
        assessment.setAssessmentLevelFive(assessmentLevelFive);
        //assessmentLevelFive.updateTotalScore();
        assessment.updateTotalScore();
        assessmentRepo.save(assessment);
        return "Assessment Level Five updated successfully";
    }

    @Transactional
    public void uploadLevelOne(MultipartFile file, int collegeId) {
        List<String> errorMessages = new ArrayList<>();
        List<String> expectedHeaders = Arrays.asList("email", "quantitativeScore", "logicalScore", "verbalScore", "codingScore");
    
        try {
            // Check if the file has the correct format
            boolean isFormatValid = excelUtil.checkExcelHeaders(file, expectedHeaders);
            if (!isFormatValid) throw new RuntimeException("Excel file format is invalid. Expected headers: " + expectedHeaders);

            List<Map<String, String>> dataList = excelUtil.readExcelFile(file);

            for (Map<String, String> data : dataList) {
                try {
                    Assessment assessment = assessmentRepo.findByEmail(data.get("email")).orElseThrow(() -> new IllegalArgumentException("Assessment not found for the email:" + data.get("email")));
                    
                    AssessmentLevelOne assessmentLevelOne = new AssessmentLevelOne();
                    if(assessment.getAssessmentLevelOne() != null)  assessmentLevelOne = assessment.getAssessmentLevelOne();
                    assessmentLevelOne.setCandidateName(assessment.getCandidateName());
                    assessmentLevelOne.setEmail(assessment.getEmail());
                    assessmentLevelOne.setQuantitativeScore((int) Double.parseDouble(data.get("quantitativeScore")));
                    assessmentLevelOne.setLogicalScore((int) Double.parseDouble(data.get("logicalScore")));
                    assessmentLevelOne.setVerbalScore((int) Double.parseDouble(data.get("verbalScore")));
                    assessmentLevelOne.setCodingScore((int) Double.parseDouble(data.get("codingScore")));
                    assessmentLevelOne.updateTotalScore();

                    assessment.setAssessmentLevelOne(assessmentLevelOne);
                    assessmentRepo.save(assessment);
                } catch (Exception e) {
                    errorMessages.add("Failed to process email " + data.get("email") + ": " + e.getMessage());
                }
            }
        }  catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        
        if (!errorMessages.isEmpty())    throw new RuntimeException("Errors occurred during processing: " + String.join(", ", errorMessages));
    }

    @Transactional
    public String selectLevelOne(List<AssessmentLevelOne> levelOneSelectedList) {
        for (AssessmentLevelOne levelOneSelected : levelOneSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelOneSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email: " + levelOneSelected.getEmail()));
                if (assessment.getAssessmentLevelTwo() == null) assessment.setAssessmentLevelTwo(new AssessmentLevelTwo());
                assessment.getAssessmentLevelTwo().setEmail(levelOneSelected.getEmail());
                assessment.getAssessmentLevelTwo().setCandidateName(levelOneSelected.getCandidateName());
                assessmentRepo.save(assessment);
                
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Two: " + e.getMessage();
            }
        }
        return "Assessment Level Two saved successfully";
    }

    @Transactional
    public String selectLevelTwo(List<AssessmentLevelTwo> levelTwoSelectedList) {
        for (AssessmentLevelTwo levelTwoSelected : levelTwoSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelTwoSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelThree() == null)   assessment.setAssessmentLevelThree(new AssessmentLevelThree());
                assessment.getAssessmentLevelThree().setEmail(levelTwoSelected.getEmail());
                assessment.getAssessmentLevelThree().setCandidateName(levelTwoSelected.getCandidateName());
                assessmentRepo.save(assessment);

            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Three: " + e.getMessage();
            }
        }
        return "Assessment Level Three saved successfully";
    }

    @Transactional
    public String selectLevelThree(List<AssessmentLevelThree> levelThreeSelectedList) {
        for (AssessmentLevelThree levelThreeSelected : levelThreeSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelThreeSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelFour() == null)    assessment.setAssessmentLevelFour(new AssessmentLevelFour());
                assessment.getAssessmentLevelFour().setEmail(levelThreeSelected.getEmail());
                assessment.getAssessmentLevelFour().setCandidateName(levelThreeSelected.getCandidateName());
                assessmentRepo.save(assessment);

            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Four: " + e.getMessage();
            }
        }
        return "Assessment Level Four saved successfully";
    }

    @Transactional
    public String selectLevelFour(List<AssessmentLevelFour> levelFourSelectedList) {
        for (AssessmentLevelFour levelFourSelected : levelFourSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelFourSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelFive() == null)    assessment.setAssessmentLevelFive(new AssessmentLevelFive());
                assessment.getAssessmentLevelFive().setEmail(levelFourSelected.getEmail());
                assessment.getAssessmentLevelFive().setCandidateName(levelFourSelected.getCandidateName());
                assessmentRepo.save(assessment);
                
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Five: " + e.getMessage();
            }
        }
        return "Assessment Level Five saved successfully";
    }

    @Transactional
    public void selectLevelFive(List<AssessmentLevelFive> levelFiveSelectedList, String authenticateUser) {
        CollegeTPO college = null;
        
        for (AssessmentLevelFive levelFiveSelected : levelFiveSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelFiveSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email: " + levelFiveSelected.getEmail()));
                if (college == null) college = assessment.getCollege();
                if (assessment.getAssessmentLevelFinal() == null)   assessment.setAssessmentLevelFinal(new AssessmentLevelFinal());
                assessment.getAssessmentLevelFinal().setEmail(levelFiveSelected.getEmail());
                assessment.getAssessmentLevelFinal().setCandidateName(levelFiveSelected.getCandidateName());
                assessmentRepo.save(assessment);

                Candidate candidate = candidateRepository.findByEmail(levelFiveSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find candidate with the provided email: " + levelFiveSelected.getEmail()));
                talentService.addTalentFromCandidate(candidate, authenticateUser); // Convert to talent
            } catch (RuntimeException e) {
                throw new RuntimeException("Error selecting candidates " + e.getMessage());
            }
        }

        if (college != null){    
            college.setAssessmentStatus("Completed");
            collegeRepository.save(college);
        }
    }
}
