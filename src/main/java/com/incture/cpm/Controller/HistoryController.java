package com.incture.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Service.HistoryService;

@RestController
@RequestMapping("/cpm/history")
public class HistoryController {
    
    @Autowired
    HistoryService historyService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllHistory(){
        return new ResponseEntity<>(historyService.getAllHistory(), HttpStatus.OK);
    }
    
    @GetMapping("/getByEntityType")
    public ResponseEntity<?> getByEntityType(@RequestParam String entityType){
        return new ResponseEntity<>(historyService.getAllHistoryByEntityType(entityType), HttpStatus.OK);
    } 

    @DeleteMapping("/deletebyEntityType")
    public ResponseEntity<?> deleteByEntityType(@RequestParam String entityType){
        historyService.deleteAllByEntityType(entityType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
