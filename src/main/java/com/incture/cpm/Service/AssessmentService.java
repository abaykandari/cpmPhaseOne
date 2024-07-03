package com.incture.cpm.Service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.AssessmentLevelFive;
import com.incture.cpm.Entity.AssessmentLevelFour;
import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Repo.AssessmentRepo;
import com.incture.cpm.Repo.CandidateRepository;

@Service
public class AssessmentService {
    @Autowired
    AssessmentRepo assessmentRepo;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    TalentService talentService;

    public Object getAllAssessments() {
        return assessmentRepo.findAll();
    }

    public AssessmentLevelOne createLevelOne(AssessmentLevelOne assessmentLevelOne) {
        String email = assessmentLevelOne.getEmail();
        candidateRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Candidate with provided email not found"));

        Assessment assessment = new Assessment();
        assessment.setEmail(email);
        assessment.setCandidateName(assessmentLevelOne.getCandidateName());
        assessment.setAssessmentLevelOne(assessmentLevelOne);
        assessmentLevelOne.updateTotalScore();
        assessmentRepo.save(assessment);

        return assessmentLevelOne;
    }

    public String createAssessment(AssessmentLevelOne assessmentLevelOne) {
        String email = assessmentLevelOne.getEmail();
        candidateRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Candidate with provided email not found"));

        Assessment assessment = new Assessment();
        assessment.setEmail(email);
        assessment.setCandidateName(assessmentLevelOne.getCandidateName());
        assessment.setAssessmentLevelOne(assessmentLevelOne);
        assessmentLevelOne.updateTotalScore();
        assessmentRepo.save(assessment);

        return "Assessment Level One saved successfully";
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

    public void save(MultipartFile file) {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0)   continue; // Skip the header row

                AssessmentLevelOne assessmentLevelOne = new AssessmentLevelOne();
                assessmentLevelOne.setCandidateName(row.getCell(0).getStringCellValue());
                assessmentLevelOne.setEmail(row.getCell(1).getStringCellValue());
                assessmentLevelOne.setQuantitativeScore((int) row.getCell(2).getNumericCellValue());
                assessmentLevelOne.setLogicalScore((int) row.getCell(3).getNumericCellValue());
                assessmentLevelOne.setVerbalScore((int) row.getCell(4).getNumericCellValue());
                assessmentLevelOne.setCodingScore((int) row.getCell(5).getNumericCellValue());
            
                createAssessment(assessmentLevelOne);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    public String selectLevelOne(List<AssessmentLevelOne> levelOneSelectedList) {
        for (AssessmentLevelOne levelOneSelected : levelOneSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelOneSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelTwo() == null) {
                    assessment.setAssessmentLevelTwo(new AssessmentLevelTwo());
                    assessment.getAssessmentLevelTwo().setEmail(levelOneSelected.getEmail());
                    assessment.getAssessmentLevelTwo().setCandidateName(levelOneSelected.getCandidateName());
                    assessmentRepo.save(assessment);
                }
                else throw new IllegalStateException("Candidate already selected");
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Two: " + e.getMessage();
            }
        }
        return "Assessment Level Two saved successfully";
    }

    public String selectLevelTwo(List<AssessmentLevelTwo> levelTwoSelectedList) {
        for (AssessmentLevelTwo levelTwoSelected : levelTwoSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelTwoSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelThree() == null) {
                    assessment.setAssessmentLevelThree(new AssessmentLevelThree());
                    assessment.getAssessmentLevelThree().setEmail(levelTwoSelected.getEmail());
                    assessment.getAssessmentLevelThree().setCandidateName(levelTwoSelected.getCandidateName());
                    assessmentRepo.save(assessment);
                }
                else throw new IllegalStateException("Candidate already selected");
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Three: " + e.getMessage();
            }
        }
        return "Assessment Level Three saved successfully";
    }

    public String selectLevelThree(List<AssessmentLevelThree> levelThreeSelectedList) {
        for (AssessmentLevelThree levelThreeSelected : levelThreeSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelThreeSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelFour() == null) {
                    assessment.setAssessmentLevelFour(new AssessmentLevelFour());
                    assessment.getAssessmentLevelFour().setEmail(levelThreeSelected.getEmail());
                    assessment.getAssessmentLevelFour().setCandidateName(levelThreeSelected.getCandidateName());
                    assessmentRepo.save(assessment);
                }
                else throw new IllegalStateException("Candidate already selected");
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Four: " + e.getMessage();
            }
        }
        return "Assessment Level Four saved successfully";
    }

    public String selectLevelFour(List<AssessmentLevelFour> levelFourSelectedList) {
        for (AssessmentLevelFour levelFourSelected : levelFourSelectedList) {
            try {
                Assessment assessment = assessmentRepo.findByEmail(levelFourSelected.getEmail()).orElseThrow(() -> new IllegalArgumentException("Could not find assessment with the provided email"));
                if (assessment.getAssessmentLevelFive() == null) {
                    assessment.setAssessmentLevelFive(new AssessmentLevelFive());
                    assessment.getAssessmentLevelFive().setEmail(levelFourSelected.getEmail());
                    assessment.getAssessmentLevelFive().setCandidateName(levelFourSelected.getCandidateName());
                    assessmentRepo.save(assessment);
                }
                else throw new IllegalStateException("Candidate already selected");
            } catch (IllegalArgumentException e) {
                return "Error adding Assessment Level Five: " + e.getMessage();
            }
        }
        return "Assessment Level Five saved successfully";
    }

    public String selectLevelFive(List<AssessmentLevelFive> levelFiveSelectedList) {
        for (AssessmentLevelFive levelFiveSelected : levelFiveSelectedList) {
            try {
                Candidate candidate = candidateRepository.findByEmail(levelFiveSelected.getEmail()).get();
                talentService.addTalentFromCandidate(candidate); // convert to talent
            } catch (IllegalArgumentException e) {
                return "Error selecting cndidates: " + e.getMessage();
            }
        }
        return "Candidates selected successfully";
    }
}
