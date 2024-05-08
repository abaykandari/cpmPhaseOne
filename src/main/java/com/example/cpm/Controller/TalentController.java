package com.example.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.cpm.Entity.Talent;
import com.example.cpm.Service.TalentService;

@RestController
@RequestMapping("/cpm/talents")
public class TalentController {

    @Autowired
    private TalentService talentService;

    @PostMapping("/createtalent")
    public ResponseEntity<Talent> createTalent(@RequestBody Talent talent) {
        Talent createdTalent = talentService.createTalent(talent);
        return new ResponseEntity<>(createdTalent, HttpStatus.CREATED);
    }

    @GetMapping("/alltalent")
    public ResponseEntity<List<Talent>> getAllTalents() {
        List<Talent> talents = talentService.getAllTalents();
        return new ResponseEntity<>(talents, HttpStatus.OK);
    }

    @GetMapping("/gettalentbyid/{talentId}")
    public ResponseEntity<Talent> getTalentById(@PathVariable int talentId) {
        Talent talent = talentService.getTalentById(talentId);
        if (talent != null) {
            return new ResponseEntity<>(talent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updatetalent/{talentId}")
    public ResponseEntity<Talent> updateTalent(@RequestBody Talent talent,@PathVariable int talentId) {
        Talent updatedTalent = talentService.updateTalent(talent,talentId);
        if (updatedTalent != null) {
            return new ResponseEntity<>(updatedTalent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deletetalent/{talentId}")
    public ResponseEntity<Void> deleteTalent(@PathVariable int talentId) {
        talentService.deleteTalent(talentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}