package com.incture.cpm.Service;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Dto.AssessmentProjection;
import com.incture.cpm.Dto.AssessmentLevelOneResponse;
import com.incture.cpm.Dto.AssessmentLevelOptionalResponse;
import com.incture.cpm.Dto.AssessmentLevelThreeResponse;
import com.incture.cpm.Dto.AssessmentLevelTwoResponse;
import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.AssessmentLevelOptional;
import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.AssessmentLevelFinal;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Repo.AssessmentLevelOneRepo;
import com.incture.cpm.Repo.AssessmentLevelOptionalRepo;
import com.incture.cpm.Repo.AssessmentLevelThreeRepo;
import com.incture.cpm.Repo.AssessmentLevelTwoRepo;
import com.incture.cpm.Repo.AssessmentRepo;
import com.incture.cpm.Repo.CandidateRepository;
import com.incture.cpm.Repo.CollegeRepository;
import com.incture.cpm.Util.ExcelUtil;

@Service
public class AssessmentService {
    @Autowired
    AssessmentRepo assessmentRepo;
    @Autowired
    AssessmentLevelOneRepo levelOneRepo;
    @Autowired
    AssessmentLevelTwoRepo levelTwoRepo;
    @Autowired
    AssessmentLevelThreeRepo levelThreeRepo;
    @Autowired
    AssessmentLevelOptionalRepo levelOptionalRepo;
    @Autowired
    CandidateRepository candidateRepo;
    @Autowired
    TalentService talentService;
    @Autowired
    CollegeRepository collegeRepo;
    @Autowired
    ExcelUtil excelUtil;

    //not to be used on frontend
    public List<Assessment> getAllAssessments() {
        return assessmentRepo.findAll();
    }

    public List<AssessmentProjection> getAllAssessmentByEkYear(String ekYear) {
        return assessmentRepo.findByEkYear(ekYear);
    }

    public Assessment getAssessmentById(long assessmentId) {
        return assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalStateException("Assessment not found for id: " + assessmentId));
    }

    public AssessmentLevelOneResponse getAssessmentLevelOneById(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalStateException("Assessment not found for id: " + assessmentId));
        return new AssessmentLevelOneResponse(assessment.getIsLevelOneCompleted(), assessment.getLevelOneList());
    }

    public AssessmentLevelTwoResponse getAssessmentLevelTwoById(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalStateException("Assessment not found for id: " + assessmentId));
        return new AssessmentLevelTwoResponse(assessment.getIsLevelTwoCompleted(), assessment.getLevelTwoList());
    }

    public AssessmentLevelThreeResponse getAssessmentLevelThreeById(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalStateException("Assessment not found for id: " + assessmentId));
        return new AssessmentLevelThreeResponse(assessment.getIsLevelThreeCompleted(), assessment.getLevelThreeList());
    }

    public AssessmentLevelOptionalResponse getAssessmentLevelOptionalById(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalStateException("Assessment not found for id: " + assessmentId));
        return new AssessmentLevelOptionalResponse(assessment.getIsLevelOptionalCompleted(), assessment.getLevelOptionalList());
    }

    public List<AssessmentLevelFinal> getAssessmentLevelFinalById(long assessmentId) {
        return getAssessmentById(assessmentId).getLevelFinalList();
    }

    public void createAssessment(int collegeId){
        String ekYear = String.valueOf(Year.now().getValue()); // create for custom year here
        Optional<Assessment> assessmentOptional = assessmentRepo.findByCollegeIdAndEkYear(collegeId, ekYear);
        if(assessmentOptional.isPresent()) throw new IllegalStateException("Assessment already exists");
        
        Assessment assessment = new Assessment();
        assessment.setCollege(collegeRepo.findById(collegeId).orElseThrow(() -> new IllegalArgumentException("Could not find college for id: " + collegeId)));
        assessment.setEkYear(ekYear);
        List<Candidate> candidates = candidateRepo.findByCollegeIdAndEkYear(collegeId, String.valueOf(ekYear));

        for(Candidate candidate : candidates){
            AssessmentLevelOne levelOne = new AssessmentLevelOne(); 
            levelOne = new AssessmentLevelOne();
            levelOne.setEmail(candidate.getEmail());
            levelOne.setCandidateName(candidate.getCandidateName());
            assessment.getLevelOneList().add(levelOne);
        }
        assessmentRepo.save(assessment);
    }

    @Transactional
    public <T> void uploadLevelExcel(MultipartFile file, long assessmentId, Function<Assessment, List<T>> getLevelList, Predicate<Assessment> isLevelCompleted, List<String> expectedHeaders, BiConsumer<Map<String, String>, T> updateFields) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (isLevelCompleted.test(assessment)) throw new IllegalStateException("Assessments are already completed and locked for further updates.");

        List<T> levelList = getLevelList.apply(assessment);
        Map<String, T> levelMap = levelList.stream().collect(Collectors.toMap(level -> {
            try {
                Method getEmailMethod = level.getClass().getMethod("getEmail");
                return (String) getEmailMethod.invoke(level);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get email from level object", e);
            }
        }, Function.identity()));

        try {
            boolean isFormatValid = excelUtil.checkExcelHeaders(file, expectedHeaders);
            if (!isFormatValid) throw new RuntimeException("Excel file format is invalid. Expected headers: " + expectedHeaders);

            List<Map<String, String>> dataList = excelUtil.readExcelFile(file);

            for (Map<String, String> data : dataList) {
                try {
                    T level = levelMap.get(data.get("Email"));
                    if (level == null) continue; // ignore data for emails not in the candidate database

                    updateFields.accept(data, level);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to process email " + data.get("Email") + ": " + e.getMessage());
                }
            }

            assessmentRepo.save(assessment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
 
    @Transactional
    public void uploadLevelOneExcel(MultipartFile file, long assessmentId) {
        List<String> expectedHeaders = Arrays.asList("Email", "Quantitative Score", "Logical Score", "Verbal Score", "Coding Score");
        uploadLevelExcel(file, assessmentId, Assessment::getLevelOneList, Assessment::getIsLevelOneCompleted, expectedHeaders, (data, level) -> {
            AssessmentLevelOne levelOne = (AssessmentLevelOne) level;
            levelOne.setQuantitativeScore(Double.parseDouble(data.get("Quantitative Score")));
            levelOne.setLogicalScore(Double.parseDouble(data.get("Logical Score")));
            levelOne.setVerbalScore(Double.parseDouble(data.get("Verbal Score")));
            levelOne.setCodingScore(Double.parseDouble(data.get("Coding Score")));
        });
    }

    @Transactional
    public void uploadLevelTwoExcel(MultipartFile file, long assessmentId) {
        List<String> expectedHeaders = Arrays.asList("Email", "Problem Statement", "Process Workflow", "Use Of Algorithms", "Tech Stacks", "Recommended Solution", "Language And Grammar", "Logical Flow");
        uploadLevelExcel(file, assessmentId, Assessment::getLevelTwoList, Assessment::getIsLevelTwoCompleted, expectedHeaders, (data, level) -> {
            AssessmentLevelTwo levelTwo = (AssessmentLevelTwo) level;
            levelTwo.setProblemStatement(Double.parseDouble(data.get("Problem Statement")));
            levelTwo.setProcessWorkflow(Double.parseDouble(data.get("Process Workflow")));
            levelTwo.setUseOfAlgorithms(Double.parseDouble(data.get("Use Of Algorithms")));
            levelTwo.setTechStacks(Double.parseDouble(data.get("Tech Stacks")));
            levelTwo.setRecommendedSolution(Double.parseDouble(data.get("Recommended Solution")));
            levelTwo.setLanguageAndGrammar(Double.parseDouble(data.get("Language And Grammar")));
            levelTwo.setLogicalFlow(Double.parseDouble(data.get("Logical Flow")));
        });
    }

    @Transactional
    public void uploadLevelThreeExcel(MultipartFile file, long assessmentId) {
        List<String> expectedHeaders = Arrays.asList("Email", "Problem Solving", "Analytical Skills", "Logical Flow", "Involved", "Team Player", "Willing To Create", "Tenacity", "Value System");
        uploadLevelExcel(file, assessmentId, Assessment::getLevelThreeList, Assessment::getIsLevelThreeCompleted, expectedHeaders, (data, level) -> {
            AssessmentLevelThree levelThree = (AssessmentLevelThree) level;
            levelThree.setProblemSolving(Double.parseDouble(data.get("Problem Solving")));
            levelThree.setAnalyticalSkills(Double.parseDouble(data.get("Analytical Skills")));
            levelThree.setLogicalFlow(Double.parseDouble(data.get("Logical Flow")));
            levelThree.setInvolved(Double.parseDouble(data.get("Involved")));
            levelThree.setTeamPlayer(Double.parseDouble(data.get("Team Player")));
            levelThree.setWillingToCreate(Double.parseDouble(data.get("Willing To Create")));
            levelThree.setTenacity(Double.parseDouble(data.get("Tenacity")));
            levelThree.setValueSystem(Double.parseDouble(data.get("Value System")));
        });
    }

    @Transactional
    public void uploadLevelOptionalExcel(MultipartFile file, long assessmentId) {
        List<String> expectedHeaders = Arrays.asList("Email", "Custom Score");
        uploadLevelExcel(file, assessmentId, Assessment::getLevelOptionalList, Assessment::getIsLevelOptionalCompleted, expectedHeaders, (data, level) -> {
            AssessmentLevelOptional levelOptional = (AssessmentLevelOptional) level;
            levelOptional.setCustomScore(Double.parseDouble(data.get("Custom Score")));
        });
    }

 
    public void updateLevel(AssessmentLevelOne updatedLevelOne, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelOneCompleted())    throw new IllegalStateException("Level One assessments are already completed and locked for further updates.");
        
        AssessmentLevelOne levelOne = assessment.getLevelOneList().stream().filter(l -> l.getEmail().equals(updatedLevelOne.getEmail())).findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find level one assessment for email: " + updatedLevelOne.getEmail()));
        levelOne.setLogicalScore(updatedLevelOne.getLogicalScore());
        levelOne.setQuantitativeScore(updatedLevelOne.getQuantitativeScore());
        levelOne.setVerbalScore(updatedLevelOne.getVerbalScore());
        levelOne.setCodingScore(updatedLevelOne.getCodingScore());
        levelOneRepo.save(levelOne);
    }

    public void updateLevel(AssessmentLevelTwo updatedLevelTwo, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelTwoCompleted()) throw new IllegalStateException("Level Two assessments are already completed and locked for further updates.");
        
        AssessmentLevelTwo levelTwo = assessment.getLevelTwoList().stream().filter(l -> l.getEmail().equals(updatedLevelTwo.getEmail())).findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find level two assessment for email: " + updatedLevelTwo.getEmail()));
        levelTwo.setProblemStatement(updatedLevelTwo.getProblemStatement());
        levelTwo.setProcessWorkflow(updatedLevelTwo.getProcessWorkflow());
        levelTwo.setUseOfAlgorithms(updatedLevelTwo.getUseOfAlgorithms());
        levelTwo.setTechStacks(updatedLevelTwo.getTechStacks());
        levelTwo.setRecommendedSolution(updatedLevelTwo.getRecommendedSolution());
        levelTwo.setLanguageAndGrammar(updatedLevelTwo.getLanguageAndGrammar());
        levelTwo.setLogicalFlow(updatedLevelTwo.getLogicalFlow());
        levelTwoRepo.save(levelTwo);
    }

    public void updateLevel(AssessmentLevelThree updatedLevelThree, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelThreeCompleted()) throw new IllegalStateException("Level Three assessments are already completed and locked for further updates.");
        
        AssessmentLevelThree levelThree = assessment.getLevelThreeList().stream().filter(l -> l.getEmail().equals(updatedLevelThree.getEmail())).findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find level three assessment for email: " + updatedLevelThree.getEmail()));
        levelThree.setProblemSolving(updatedLevelThree.getProblemSolving());
        levelThree.setAnalyticalSkills(updatedLevelThree.getAnalyticalSkills());
        levelThree.setLogicalFlow(updatedLevelThree.getLogicalFlow());
        levelThree.setInvolved(updatedLevelThree.getInvolved());
        levelThree.setTeamPlayer(updatedLevelThree.getTeamPlayer());
        levelThree.setWillingToCreate(updatedLevelThree.getWillingToCreate());
        levelThree.setTenacity(updatedLevelThree.getTenacity());
        levelThree.setValueSystem(updatedLevelThree.getValueSystem());
        levelThreeRepo.save(levelThree);
    }

    public void updateLevel(AssessmentLevelOptional updatedLevelOptional, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelOptionalCompleted()) throw new IllegalStateException("Level Optional assessments are already completed and locked for further updates.");
        
        AssessmentLevelOptional levelOptional = assessment.getLevelOptionalList().stream().filter(l -> l.getEmail().equals(updatedLevelOptional.getEmail())).findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find level optional assessment for email: " + updatedLevelOptional.getEmail()));
        levelOptional.setCustomScore(updatedLevelOptional.getCustomScore());
        levelOptionalRepo.save(updatedLevelOptional);
    }

    public void selectLevelOneCandidates(List<AssessmentLevelOne> levelOneSelectionList, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelOneCompleted())    throw new IllegalStateException("Level One assessments are already completed and locked for further updates.");
        
        Map<String, AssessmentLevelOne> levelOneMap = assessment.getLevelOneList().stream().collect(Collectors.toMap(AssessmentLevelOne::getEmail, Function.identity()));
        
        for (AssessmentLevelOne levelOneSelection : levelOneSelectionList) {
            AssessmentLevelOne levelOne = levelOneMap.get(levelOneSelection.getEmail());
            if(levelOne != null)    levelOne.setSelected(true);
        }
        assessmentRepo.save(assessment);
    }

    public void selectLevelTwoCandidates(List<AssessmentLevelTwo> levelTwoSelectionList, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelTwoCompleted())    throw new IllegalStateException("Level Two assessments are already completed and locked for further updates.");
        
        Map<String, AssessmentLevelTwo> levelTwoMap = assessment.getLevelTwoList().stream().collect(Collectors.toMap(AssessmentLevelTwo::getEmail, Function.identity()));
        
        for (AssessmentLevelTwo levelTwoSelection : levelTwoSelectionList) {
            AssessmentLevelTwo levelTwo = levelTwoMap.get(levelTwoSelection.getEmail());
            if(levelTwo != null)    levelTwo.setSelected(true);
        }
        
        assessmentRepo.save(assessment);
    }

    public void selectLevelThreeCandidates(List<AssessmentLevelThree> levelThreeSelectionList, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelThreeCompleted()) throw new IllegalStateException("Level Three assessments are already completed and locked for further updates.");
    
        Map<String, AssessmentLevelThree> levelThreeMap = assessment.getLevelThreeList().stream().collect(Collectors.toMap(AssessmentLevelThree::getEmail, Function.identity()));
    
        for (AssessmentLevelThree levelThreeSelection : levelThreeSelectionList) {
            AssessmentLevelThree levelThree = levelThreeMap.get(levelThreeSelection.getEmail());
            if (levelThree != null) levelThree.setSelected(true);
        }
        assessmentRepo.save(assessment);
    }
    
    public void selectLevelOptionalCandidates(List<AssessmentLevelOptional> levelOptionalSelectionList, long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelOptionalCompleted()) throw new IllegalStateException("Level Optional assessments are already completed and locked for further updates.");
    
        Map<String, AssessmentLevelOptional> levelOptionalMap = assessment.getLevelOptionalList().stream().collect(Collectors.toMap(AssessmentLevelOptional::getEmail, Function.identity()));
    
        for (AssessmentLevelOptional levelOptionalSelection : levelOptionalSelectionList) {
            AssessmentLevelOptional levelOptional = levelOptionalMap.get(levelOptionalSelection.getEmail());
            if (levelOptional != null) levelOptional.setSelected(true);
        }
        assessmentRepo.save(assessment);
    }

    @Transactional
    public void markLevelOneAsComplete(long assessmentId){
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));

        List<AssessmentLevelOne> levelOneList = assessment.getLevelOneList();
        List<AssessmentLevelTwo> levelTwoList = new ArrayList<>();

        for (AssessmentLevelOne levelOne : levelOneList) {
            if(levelOne.isSelected() == false) continue;
            AssessmentLevelTwo levelTwo = new AssessmentLevelTwo();
            levelTwo.setEmail(levelOne.getEmail());
            levelTwo.setCandidateName(levelOne.getCandidateName());
            levelTwoList.add(levelTwo);
        }

        assessment.getLevelTwoList().addAll(levelTwoList);
        assessment.setIsLevelOneCompleted(true);
    }

    @Transactional
    public void markLevelTwoAsComplete(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));

        List<AssessmentLevelTwo> levelTwoList = assessment.getLevelTwoList().stream().filter(AssessmentLevelTwo::isSelected).collect(Collectors.toList()); 
        List<AssessmentLevelThree> levelThreeList = new ArrayList<>();

        for (AssessmentLevelTwo levelTwo : levelTwoList) {
            AssessmentLevelThree levelThree = new AssessmentLevelThree();
            levelThree.setEmail(levelTwo.getEmail());
            levelThree.setCandidateName(levelTwo.getCandidateName());
            levelThreeList.add(levelThree);
        }

        assessment.getLevelThreeList().addAll(levelThreeList);
        assessment.setIsLevelTwoCompleted(true);
    }

    @Transactional
    public void markLevelThreeAsComplete(long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));

        List<AssessmentLevelThree> levelThreeList = assessment.getLevelThreeList().stream().filter(AssessmentLevelThree::isSelected).collect(Collectors.toList()); 
        List<AssessmentLevelOptional> levelOptionalList = new ArrayList<>();

        for (AssessmentLevelThree levelThree : levelThreeList) {
            AssessmentLevelOptional levelOptional = new AssessmentLevelOptional();
            levelOptional.setEmail(levelThree.getEmail());
            levelOptional.setCandidateName(levelThree.getCandidateName());
            levelOptionalList.add(levelOptional);
        }

        assessment.getLevelOptionalList().addAll(levelOptionalList);
        assessment.setIsLevelThreeCompleted(true);
    }

    @Transactional
    public void skipOptionalLevel(long assessmentId, String authenticatedUser){
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));
        if (assessment.getIsLevelOptionalCompleted()) throw new IllegalStateException("Level Optional assessments are already completed and locked for further updates.");
        
        List<AssessmentLevelOptional> levelOptionalList = assessment.getLevelOptionalList();
        for (AssessmentLevelOptional levelOptional : levelOptionalList)    levelOptional.setSelected(true);
        
        markLevelOptionalAsComplete(assessmentId, authenticatedUser);
    }

    @Transactional
    public void markLevelOptionalAsComplete(long assessmentId, String authenticatedUser) {
        Assessment assessment = assessmentRepo.findById(assessmentId).orElseThrow(() -> new IllegalArgumentException("Could not find assessment for assessment id: " + assessmentId));

        List<AssessmentLevelOptional> levelOptionalList = assessment.getLevelOptionalList().stream().filter(AssessmentLevelOptional::isSelected).collect(Collectors.toList());
        List<AssessmentLevelFinal> levelFinalList = new ArrayList<>();

        for (AssessmentLevelOptional levelOptional : levelOptionalList) {
            AssessmentLevelFinal levelFinal = new AssessmentLevelFinal();
            levelFinal.setEmail(levelOptional.getEmail());
            levelFinal.setCandidateName(levelOptional.getCandidateName());
            levelFinalList.add(levelFinal);
        }

        assessment.getLevelFinalList().addAll(levelFinalList);
        assessment.setIsLevelOptionalCompleted(true);
        convertToTalent(levelFinalList, authenticatedUser);
    }

    public void convertToTalent(List<AssessmentLevelFinal> levelFinalList, String authenticatedUser){
        List<String> emails = levelFinalList.stream().map(AssessmentLevelFinal::getEmail).collect(Collectors.toList());
        List<Candidate> candidateList = candidateRepo.findByEmailIn(emails);
        talentService.addTalentList(candidateList, authenticatedUser);
    }

    @Transactional
    public void deleteAssessmentById(long assessmentId){
        assessmentRepo.deleteById(assessmentId);
    }    
    
    @Transactional
    public void truncateAssessments() {
        assessmentRepo.deleteAll();
    }
}