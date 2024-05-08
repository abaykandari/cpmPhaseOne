package com.example.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cpm.Entity.Regularize;
import com.example.cpm.Service.RegularizeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/regularize")
public class RegularizeController {

    @Autowired
    private RegularizeService employeeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Regularize>> getAllEmployees() {
        List<Regularize> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Regularize> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Regularize> employee = employeeService.getEmployeeById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Regularize> createEmployee(@RequestBody Regularize employee) {
        Regularize createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Regularize> updateEmployee(@PathVariable("id") Long id, @RequestBody Regularize updatedEmployee) {
        Regularize employee = employeeService.updateEmployee(id, updatedEmployee);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
