package com.incture.cpm.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Leaves;
import com.incture.cpm.Service.LeavesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/leaves")
@Tag(name = "Leaves", description = "Endpoints for managing leave records")
public class LeavesController {

    @Autowired
    private LeavesService leavesService;

    @Operation(summary = "Get All Leaves", description = "Retrieve all leave records. Accessible only to admins.")
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Leaves> getLeaves() {
        return leavesService.getAll();
    }

    @Operation(summary = "Add Leave with File", description = "Add a leave record along with an optional file attachment.")
    @PostMapping("/addLeave")
    public ResponseEntity<String> addLeave(@RequestPart("leave") Leaves leave,
                                    @RequestPart("file") MultipartFile file) throws SerialException, SQLException, IOException {
        String message = leavesService.addLeave(leave, file);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Add Multiple Leaves", description = "Add multiple leave records at once. Accessible only to admins.")
    @PostMapping("/addLeaves")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addLeaves(@RequestBody List<Leaves> leaves) {
        String message = leavesService.addLeaves(leaves);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Approve Leave", description = "Approve a leave request by its ID. Accessible only to admins.")
    @PutMapping("/approve/{leaveId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long leaveId) {
        String message = leavesService.approve(leaveId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Operation(summary = "Decline Leave", description = "Decline a leave request by its ID with a reason for rejection. Accessible only to admins.")
    @PutMapping("/decline/{leaveId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> decline(@PathVariable Long leaveId, @RequestParam String reasonForReject) {
        String message = leavesService.decline(leaveId, reasonForReject);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
