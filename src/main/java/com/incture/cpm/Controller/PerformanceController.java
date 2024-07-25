package com.incture.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Performance;
import com.incture.cpm.Exception.BadRequestException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.PerformanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/performance")
@Tag(name = "Performance", description = "Endpoints for managing performance records")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @Operation(summary = "Get All Performances", description = "Retrieve all performance records. Accessible only to admins.")
    @GetMapping("/getAllPerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Performance> getAllPerformances() {
        return performanceService.getAllPerformances();
    }

    @Operation(summary = "Get Performance by ID", description = "Retrieve a performance record by its ID. Requires a valid Talent ID.")
    @GetMapping("/getPerformanceById")
    public ResponseEntity<Performance> getPerformanceById(@RequestParam Long talentId) {
        if (talentId == null) {
            throw new BadRequestException("Talent ID is required.");
        }
        try {
            Performance performance = performanceService.getPerformanceById(talentId);
            if (performance == null) {
                throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
            }
            return new ResponseEntity<>(performance, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Performance", description = "Update a performance record. Requires valid performance data.")
    @PostMapping("/updatePerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updatePerformance(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            String message = performanceService.updatePerformance(performance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add Performance Records by List", description = "Add multiple performance records at once. Requires a list of performance data.")
    @PostMapping("/addPerformanceByList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addPerformanceByList(@RequestBody List<Performance> performanceList) {
        if (performanceList == null || performanceList.isEmpty()) {
            throw new BadRequestException("Performance list is required.");
        }
        try {
            String message = performanceService.addPerformanceByList(performanceList);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Performance Feedback", description = "Update feedback for a performance record. Requires valid performance data.")
    @PostMapping("/updateFeedback")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateFeedback(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            performanceService.updateFeedback(performance);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete Performance Record", description = "Delete a performance record. Requires valid performance data.")
    @DeleteMapping("/deletePerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePerformance(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            performanceService.deletePerformance(performance);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete Performance by ID", description = "Delete a performance record by its ID. Requires a valid Talent ID.")
    @DeleteMapping("/deletePerformanceById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePerformance(@RequestParam Long talentId) {
        if (talentId == null) {
            throw new BadRequestException("Talent ID is required.");
        }
        try {
            performanceService.deletePerformance(talentId);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
