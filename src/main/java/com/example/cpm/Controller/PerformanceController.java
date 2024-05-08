package com.example.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cpm.Entity.Performance;
import com.example.cpm.Service.PerformanceService;

@RestController
@RequestMapping("/cpm/performance")
public class PerformanceController {
    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/getAllPerformance")
    public List<Performance> getAllPerformances() {
        return performanceService.getAllPerformances();
    }

    @PostMapping("/addPerformance")
    public ResponseEntity<String> addPerformance(@RequestBody Performance performance) {
        String message=performanceService.addPerformance(performance);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/updateFeedback")
    public ResponseEntity<String> updateFeedback(@RequestBody Performance performance) {
        performanceService.updateFeedback(performance);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("/deletePerformance")
    public ResponseEntity<String> deletePerformance(@RequestBody Performance performance){
        performanceService.deletePerformance(performance);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("/deletePerformanceById")
    public ResponseEntity<String> deletePerformance(@RequestParam int talentId){
        performanceService.deletePerformance(talentId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
