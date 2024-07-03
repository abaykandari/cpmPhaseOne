package com.incture.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Leaves;
import com.incture.cpm.Service.LeavesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/leaves")
public class LeavesController {
    @Autowired
    private LeavesService leavesService;

    @GetMapping("/getAll")
    public List<Leaves> getLeaves(){
        return leavesService.getAll();
    }

    @PostMapping("/addLeave")
    public ResponseEntity<String> addLeave(@RequestBody Leaves leave){
        String message = leavesService.addLeave(leave);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/addLeaves")
    public ResponseEntity<String> addLeaves(@RequestBody List<Leaves> leaves){
        String message = leavesService.addLeaves(leaves);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<String> approve(@PathVariable("leaveId") Long leaveId){
        String message = leavesService.approve(leaveId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/decline/{leaveId}")
    public ResponseEntity<String> decline(@PathVariable("leaveId") Long leaveId){
        String message = leavesService.decline(leaveId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
