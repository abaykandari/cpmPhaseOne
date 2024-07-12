package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Training;
import com.incture.cpm.Repo.TrainingRepo;
import com.incture.cpm.Service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/training")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Training> addFunction(@RequestBody Training training){
        return  ResponseEntity.ok(trainingService.insertFunction(training));
    }
    @GetMapping("/read")
    public ResponseEntity<List<Training>> readFunction(){
        return ResponseEntity.ok(trainingService.readFunction());
    }
}
