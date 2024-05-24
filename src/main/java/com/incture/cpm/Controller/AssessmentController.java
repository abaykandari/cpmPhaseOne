package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Service.AssessmentService;
import com.incture.cpm.helper.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/getalltalents")
    public ResponseEntity<List<Talent>> getAllTalentsForAssessment() {
        List<Talent> talents = assessmentService.getAllTalentsForAssessment();
        return ResponseEntity.ok(talents);
    }

    @PostMapping("/addassessment")
    public ResponseEntity<String> addAssessment(@RequestBody Assessment assessment) {
        String response = assessmentService.addAssessment(assessment);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewassessment/{talentId}")
    public ResponseEntity<List<Assessment>> viewAssessmentsForTalent(@PathVariable Long talentId) {
        List<Assessment> assessments = assessmentService.viewAssessmentsForTalent(talentId);
        if (assessments != null) {
            return ResponseEntity.ok(assessments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateassessment/{assessmentId}/{talentId}")
    public ResponseEntity<Assessment> updateAssessment(@PathVariable Long assessmentId, @PathVariable Long talentId,
            @RequestBody Assessment updatedAssessment) {
        Assessment assessment = assessmentService.updateAssessment(assessmentId, talentId, updatedAssessment);
        if (assessment != null) {
            return ResponseEntity.ok(assessment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteassessment/{assessmentId}/{talentId}")
    public ResponseEntity<String> deleteAssessment(@PathVariable Long assessmentId, @PathVariable Long talentId) {
        boolean deleted = assessmentService.deleteAssessment(assessmentId, talentId);
        if (deleted) {
            return ResponseEntity.ok("Assessment deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assessment not found");
        }
    }

    //********************Uploading Excel file for Assessment Record*************************** */

    @PostMapping("/uploadexcel")
    public ResponseEntity<?> uploadExcelFile(@RequestPart MultipartFile file){
        if(Helper.checkExcelFormat(file)){
            this.assessmentService.save(file);

            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to Database"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
    }
}