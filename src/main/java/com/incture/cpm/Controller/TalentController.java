package com.incture.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Service.PerformanceService;
import com.incture.cpm.Service.TalentService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/talents")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @Autowired
    private PerformanceService performanceService;

    @PostMapping("/createtalent")
    public ResponseEntity<Talent> createTalent(@RequestBody Talent talent) {
        Talent createdTalent = talentService.createTalent(talent);
        return new ResponseEntity<>(createdTalent, HttpStatus.CREATED);
    }

    @PostMapping("/addtalentfromcandidate")
    public ResponseEntity<Talent> addTalentFromCandidate(@RequestBody Candidate candidate) {
        Talent newtalent = talentService.addTalentFromCandidate(candidate);
        if (newtalent == null) {
            return new ResponseEntity<>(newtalent, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newtalent, HttpStatus.CREATED);
    }

    @GetMapping("/alltalent")
    public ResponseEntity<List<Talent>> getAllTalents() {
        List<Talent> talents = talentService.getAllTalents();
        return new ResponseEntity<>(talents, HttpStatus.OK);
    }

    @GetMapping("/gettalentbyid/{talentId}")
    public ResponseEntity<Talent> getTalentById(@PathVariable Long talentId) {
        Talent talent = talentService.getTalentById(talentId); 
        
        if (talent != null) {
            return new ResponseEntity<>(talent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updatetalent/{talentId}")
    public ResponseEntity<Talent> updateTalent(@RequestBody Talent talent, @PathVariable Long talentId) {
        Talent updatedTalent = talentService.updateTalent(talent, talentId);
        
        if (updatedTalent != null) {
            performanceService.editTalentDetails(talent); // edit details in performance too (but not in attendance)
            return new ResponseEntity<>(updatedTalent, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deletetalent/{talentId}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Long talentId) {
        talentService.deleteTalent(talentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}