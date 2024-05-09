package com.example.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cpm.Entity.Regularize;
import com.example.cpm.Service.AttendanceService;
import com.example.cpm.Service.RegularizeService;

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

    @GetMapping("/getById/{id}")
    public ResponseEntity<Regularize> getRegularizeById(@PathVariable("id") int id) {
        Optional<Regularize> employee = regularizeService.getRegularizeById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addRegularize")
    public ResponseEntity<String> createRegularize(@RequestBody Regularize employee) {
        String message = regularizeService.createRegularize(employee);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/updateRegularize/{id}")
    public ResponseEntity<String> updateRegularize(@PathVariable("id") int id) {
        String message = attendanceService.updateRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/deleteRegularize/{id}") 
    public ResponseEntity<String> deleteRegularize(@PathVariable("id") int id) {
        String message = regularizeService.deleteRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
