package com.incture.cpm.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelOptional;
import com.incture.cpm.Service.AssessmentService;
import com.incture.cpm.Util.ExcelUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cpm/assessment")
@CrossOrigin("*")
@Tag(name = "Candidate Assessment", description = "Endpoints for managing candidate assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "Get All Assessments", description = "Retrieve all assessments.")
    @GetMapping("/getAllAssessments")
    //not to be used on frontend
    public ResponseEntity<?> getAllAssessments(){
        return new ResponseEntity<>(assessmentService.getAllAssessments(), HttpStatus.OK);
    }

    @Operation(summary = "Get Assessments by Ek Year", description = "Retrieve assessments by the specified Ek Year")
    @GetMapping("/getAllAssessmentByEkYear")
    public ResponseEntity<?> getAllAssessmentByEkYear(@RequestParam String ekYear){
        return new ResponseEntity<>(assessmentService.getAllAssessmentByEkYear(ekYear), HttpStatus.OK);
    }

    @Operation(summary = "Get Assessment or Specific Level by ID", description = "Retrieve an assessment or a specific level of an assessment by ID.")
    @GetMapping("/getAssessmentLevel")
    public ResponseEntity<?> getAssessmentLevel(
            @RequestParam(required = false) String level,
            @RequestParam long assessmentId) {
        try {
            if (level == null)  return new ResponseEntity<>(assessmentService.getAssessmentById(assessmentId), HttpStatus.OK);
            
            switch (level.toLowerCase()) {
                case "levelone":
                    return new ResponseEntity<>(assessmentService.getAssessmentLevelOneById(assessmentId), HttpStatus.OK);
                case "leveltwo":
                    return new ResponseEntity<>(assessmentService.getAssessmentLevelTwoById(assessmentId), HttpStatus.OK);
                case "levelthree":
                    return new ResponseEntity<>(assessmentService.getAssessmentLevelThreeById(assessmentId), HttpStatus.OK);
                case "leveloptional":
                    return new ResponseEntity<>(assessmentService.getAssessmentLevelOptionalById(assessmentId), HttpStatus.OK);
                case "levelfinal":
                    return new ResponseEntity<>(assessmentService.getAssessmentLevelFinalById(assessmentId), HttpStatus.OK);
                default:
                    return new ResponseEntity<>("Invalid level specified", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @Operation(summary = "Load Candiates Data", description = "Load candidates data and start their assessment")
    @PostMapping("/createAssessment")
    public ResponseEntity<?> createAssessment(@RequestParam int collegeId){
        try{
            assessmentService.createAssessment(collegeId);
            return new ResponseEntity<>("OK", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/uploadLevelExcel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam long assessmentId, @RequestParam String level) {
        try {
            switch (level.toLowerCase()) {
                case "levelone":
                    assessmentService.uploadLevelOneExcel(file, assessmentId);
                    break;
                case "leveltwo":
                    assessmentService.uploadLevelTwoExcel(file, assessmentId);
                    break;
                case "levelthree":
                    assessmentService.uploadLevelThreeExcel(file, assessmentId);
                    break;
                case "leveloptional":
                    assessmentService.uploadLevelOptionalExcel(file, assessmentId);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid level specified: " + level);
            }
            return new ResponseEntity<>("Upload successful", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Assessment Level", description = "Update an assessment record for a specific level.")
    @PutMapping("/updateAssessment")
    public ResponseEntity<String> updateAssessment(
            @RequestParam String level,
            @RequestPart Object levelData,
            @RequestParam long assessmentId) {
        try {
            switch (level.toLowerCase()) {
                case "levelone":
                    AssessmentLevelOne levelOne = objectMapper.convertValue(levelData, AssessmentLevelOne.class);
                    assessmentService.updateLevel(levelOne, assessmentId);
                    break;
                case "leveltwo":
                    AssessmentLevelTwo levelTwo = objectMapper.convertValue(levelData, AssessmentLevelTwo.class);
                    assessmentService.updateLevel(levelTwo, assessmentId);
                    break;
                case "levelthree":
                    AssessmentLevelThree levelThree = objectMapper.convertValue(levelData, AssessmentLevelThree.class);
                    assessmentService.updateLevel(levelThree, assessmentId);
                    break;
                case "leveloptional":
                    AssessmentLevelOptional levelOptional = objectMapper.convertValue(levelData, AssessmentLevelOptional.class);
                    assessmentService.updateLevel(levelOptional, assessmentId);
                    break;
                default:
                    return new ResponseEntity<>("Invalid level specified", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/selectCandidates")
    public ResponseEntity<String> selectCandidates(@RequestParam String level, @RequestParam long assessmentId, @RequestPart List<Object> selectionList) {
        try {
            switch (level.toLowerCase()) {
                case "levelone":
                    List<AssessmentLevelOne> levelOneSelectionList = objectMapper.convertValue(selectionList, new TypeReference<List<AssessmentLevelOne>>() {});
                    assessmentService.selectLevelOneCandidates(levelOneSelectionList, assessmentId);
                    break;
                case "leveltwo":
                    List<AssessmentLevelTwo> levelTwoSelectionList = objectMapper.convertValue(selectionList, new TypeReference<List<AssessmentLevelTwo>>() {});
                    assessmentService.selectLevelTwoCandidates(levelTwoSelectionList, assessmentId);
                    break;
                case "levelthree":
                    List<AssessmentLevelThree> levelThreeSelectionList = objectMapper.convertValue(selectionList, new TypeReference<List<AssessmentLevelThree>>() {});
                    assessmentService.selectLevelThreeCandidates(levelThreeSelectionList, assessmentId);
                    break;
                case "leveloptional":
                    List<AssessmentLevelOptional> levelOptionalSelectionList = objectMapper.convertValue(selectionList, new TypeReference<List<AssessmentLevelOptional>>() {});
                    assessmentService.selectLevelOptionalCandidates(levelOptionalSelectionList, assessmentId);
                    break;
                default:
                    return new ResponseEntity<>("Invalid level specified", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Candidates selected successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Mark Assessment Level as Complete", description = "Mark a specified level of an assessment as complete.")
    @PutMapping("/markLevelAsComplete")
    public ResponseEntity<String> markLevelAsComplete(@RequestParam String level, @RequestParam long assessmentId) {
        try {
            switch (level.toLowerCase()) {
                case "levelone":
                    assessmentService.markLevelOneAsComplete(assessmentId);
                    break;
                case "leveltwo":
                    assessmentService.markLevelTwoAsComplete(assessmentId);
                    break;
                case "levelthree":
                    assessmentService.markLevelThreeAsComplete(assessmentId);
                    break;
                case "leveloptional":
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    assessmentService.markLevelOptionalAsComplete(assessmentId, authentication.getName());
                    break;
                default:
                    return new ResponseEntity<>("Invalid level specified", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/skipOptionalLevel")
    public ResponseEntity<String> skipOptionalLevel(@RequestParam long assessmentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            assessmentService.skipOptionalLevel(assessmentId, authentication.getName());
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Read Excel File", description = "Read an Excel file and return its content as a list of maps.")
    @PostMapping("/readExcel")
    public ResponseEntity<List<Map<String, String>>> readExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(excelUtil.readExcelFile(file));
    }

    @DeleteMapping("/deleteAssessmentById")
    public ResponseEntity<?> deleteAssessmentById(@RequestParam long assessmentId){
        assessmentService.deleteAssessmentById(assessmentId);
        return new ResponseEntity<>("Assessment deleted successfully", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/truncate")
    public ResponseEntity<?> truncateAssessments(){
        assessmentService.truncateAssessments();
        return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);
    }

}