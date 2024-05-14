package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incture.cpm.Entity.Regularize;
import com.incture.cpm.Service.AttendanceService;
import com.incture.cpm.Service.RegularizeService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("cpm/regularize")
public class RegularizeController {

    @Autowired
    private RegularizeService regularizeService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Regularize>> getAllRegularization() {
        List<Regularize> employees = regularizeService.getAllRegularization();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/getAllPending")
    public ResponseEntity<List<Regularize>> getAllPendingRegularization() {
        List<Regularize> employees = regularizeService.getAllPendingRegularization();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Regularize> getRegularizeById(@PathVariable("id") Long id) {
        Optional<Regularize> employee = regularizeService.getRegularizeById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addRegularize")
    public ResponseEntity<String> createRegularize(@RequestBody Regularize employee) {
        String message = regularizeService.createRegularize(employee);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/addRegularizeByList")
    public ResponseEntity<String> createRegularizeList(@RequestBody List<Regularize> regularizeList) {
        String message = regularizeService.createRegularizeList(regularizeList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/approveRegularize/{id}")
    public ResponseEntity<String> approveRegularize(@PathVariable("id") Long id) {
        String message = attendanceService.approveRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/declineRegularize/{id}")
    public ResponseEntity<String> declineRegularize(@PathVariable("id") Long id) {
        String message = regularizeService.declineRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/deleteRegularize/{id}") 
    public ResponseEntity<String> deleteRegularize(@PathVariable("id") Long id) {
        String message = regularizeService.deleteRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
