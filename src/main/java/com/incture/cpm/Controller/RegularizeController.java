package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.incture.cpm.Entity.Regularize;
import com.incture.cpm.Service.AttendanceService;
import com.incture.cpm.Service.RegularizeService;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("cpm/regularize")
@Tag(name = "Regularize", description = "Endpoints for managing regularization of attendance")
public class RegularizeController {

    @Autowired
    private RegularizeService regularizeService;

    @Autowired
    private AttendanceService attendanceService;

    @Operation(summary = "Get All Regularizations", description = "Retrieve all regularization records. Accessible only to admins.")
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Regularize>> getAllRegularization() {
        List<Regularize> employees = regularizeService.getAllRegularization();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get All Pending Regularizations", description = "Retrieve all pending regularization records. Accessible only to admins.")
    @GetMapping("/getAllPending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Regularize>> getAllPendingRegularization() {
        List<Regularize> employees = regularizeService.getAllPendingRegularization();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get Regularization by ID", description = "Retrieve a regularization record by its ID.")
    @GetMapping("/getById/{id}")
    public ResponseEntity<Regularize> getRegularizeById(@PathVariable Long id) {
        Optional<Regularize> employee = regularizeService.getRegularizeById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Get Regularizations by Talent ID", description = "Retrieve all regularization records associated with a specific talent ID.")
    @GetMapping("/getByTalentId/{talentId}")
    public ResponseEntity<List<Regularize>> getRegularizeByTalentId(@PathVariable Long talentId) {
        Optional<List<Regularize>> employee = regularizeService.getRegularizeByTalentId(talentId);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Add Regularization", description = "Create a new regularization record.")
    @PostMapping("/addRegularize")
    public ResponseEntity<String> createRegularize(@RequestBody Regularize employee) {
        String message = regularizeService.createRegularize(employee);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Add Multiple Regularizations", description = "Create multiple regularization records at once. Accessible only to admins.")
    @PostMapping("/addRegularizeByList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createRegularizeList(@RequestBody List<Regularize> regularizeList) {
        String message = regularizeService.createRegularizeList(regularizeList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Approve Regularization", description = "Approve a regularization request by its ID. Accessible only to admins.")
    @PutMapping("/approveRegularize/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approveRegularize(@PathVariable Long id) {
        String message = attendanceService.approveRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Decline Regularization", description = "Decline a regularization request by its ID. Accessible only to admins.")
    @PutMapping("/declineRegularize/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> declineRegularize(@PathVariable Long id) {
        String message = regularizeService.declineRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Delete Regularization", description = "Delete a regularization record by its ID. Accessible only to admins.")
    @DeleteMapping("/deleteRegularize/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteRegularize(@PathVariable Long id) {
        String message = regularizeService.deleteRegularize(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
