package com.incture.cpm.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelFour;
import com.incture.cpm.Entity.AssessmentLevelFive;
import com.incture.cpm.Service.AssessmentService;
import com.incture.cpm.Util.ExcelUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cpm2/assessment")
@CrossOrigin("*")
@Tag(name = "Candidate Assessment", description = "Endpoints for managing candidate assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private ExcelUtil excelUtil;


    @Operation(summary = "Load Candiates Data", description = "Load candidates data and start their assessment")
    @PostMapping("/loadCandidates")
    public ResponseEntity<?> loadCandidates(@RequestParam int collegeId){
        return new ResponseEntity<>(assessmentService.loadCandidates(collegeId), HttpStatus.OK);
    }

    @Operation(summary = "Upload Level One Data", description = "Upload an Excel file for Level One assessments and save data to the database.")
    @PostMapping("/uploadLevelOne")
    public ResponseEntity<?> uploadLevelOne(@RequestParam MultipartFile file, @RequestParam int collegeId){
        if (file.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file");
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            this.assessmentService.uploadLevelOne(file, collegeId);
            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid Excel file");
        } 
    }

    @Operation(summary = "Get Assessments by College ID", description = "Retrieve assessments by the specified college ID.")
    @GetMapping("/getAssessmentByCollegeId")
    public ResponseEntity<?> getAssessmentByCollegeId(@RequestParam int collegeId){
        return new ResponseEntity<>(assessmentService.getAssessmentByCollegeId(collegeId), HttpStatus.OK);
    }

    @Operation(summary = "Get All Assessments", description = "Retrieve all assessments.")
    @GetMapping("/getAllAssessments")
    public ResponseEntity<?> getAllAssessments(){
        return new ResponseEntity<>(assessmentService.getAllAssessments(), HttpStatus.OK);
    }

    @Operation(summary = "Select Level One Assessments", description = "Select assessments for Level One.")
    @PostMapping("/selectLevelOne")
    public ResponseEntity<String> selectLevelOne(@RequestBody List<AssessmentLevelOne> levelOneSelectedList){
        String message = assessmentService.selectLevelOne(levelOneSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Select Level Two Assessments", description = "Select assessments for Level Two.")
    @PostMapping("/selectLevelTwo")
    public ResponseEntity<String> selectLevelTwo(@RequestBody List<AssessmentLevelTwo> levelTwoSelectedList){
        String message = assessmentService.selectLevelTwo(levelTwoSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Select Level Three Assessments", description = "Select assessments for Level Three.")
    @PostMapping("/selectLevelThree")
    public ResponseEntity<String> selectLevelThree(@RequestBody List<AssessmentLevelThree> levelThreeSelectedList){
        String message = assessmentService.selectLevelThree(levelThreeSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Select Level Four Assessments", description = "Select assessments for Level Four.")
    @PostMapping("/selectLevelFour")
    public ResponseEntity<String> selectLevelFour(@RequestBody List<AssessmentLevelFour> levelFourSelectedList){
        String message = assessmentService.selectLevelFour(levelFourSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Select Level Five Assessments", description = "Select assessments for Level Five.")
    @PostMapping("/selectLevelFive")
    public ResponseEntity<?> selectLevelFive(@RequestBody List<AssessmentLevelFive> levelFiveSelectedList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assessmentService.selectLevelFive(levelFiveSelectedList, authentication.getName());
        return new ResponseEntity<>("Candidates selected successfully", HttpStatus.OK);
    }

    @Operation(summary = "Update Level One Assessment", description = "Update a Level One assessment record.")
    @PutMapping("/updateLevelOne")
    public ResponseEntity<String> updateLevelOne(@RequestBody AssessmentLevelOne levelOne){
        try {
            String message = assessmentService.updateLevelOne(levelOne);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Level Two Assessment", description = "Update a Level Two assessment record.")
    @PutMapping("/updateLevelTwo")
    public ResponseEntity<String> updateLevelTwo(@RequestBody AssessmentLevelTwo levelTwo){
        try {
            String message = assessmentService.updateLevelTwo(levelTwo);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Level Three Assessment", description = "Update a Level Three assessment record.")
    @PutMapping("/updateLevelThree")
    public ResponseEntity<String> updateLevelThree(@RequestBody AssessmentLevelThree levelThree){
        try {
            String message = assessmentService.updateLevelThree(levelThree);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Level Four Assessment", description = "Update a Level Four assessment record.")
    @PutMapping("/updateLevelFour")
    public ResponseEntity<String> updateLevelFour(@RequestBody AssessmentLevelFour levelFour){
        try {
            String message = assessmentService.updateLevelFour(levelFour);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Level Five Assessment", description = "Update a Level Five assessment record.")
    @PutMapping("/updateLevelFive")
    public ResponseEntity<String> updateLevelFive(@RequestBody AssessmentLevelFive levelFive){
        try {
            String message = assessmentService.updateLevelFive(levelFive);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Read Excel File", description = "Read an Excel file and return its content as a list of maps.")
    @PostMapping("/readExcel")
    public ResponseEntity<List<Map<String, String>>> readExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(excelUtil.readExcelFile(file));
    }
}
