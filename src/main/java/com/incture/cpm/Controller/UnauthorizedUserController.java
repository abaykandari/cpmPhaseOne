package com.incture.cpm.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Dto.UnauthorizedUserWithHistory;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Service.HistoryService;
import com.incture.cpm.Service.UnauthorizedUserService;
import com.incture.cpm.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/super/security")
@Slf4j
@Tag(name = "SuperAdmin", description = "Superadmin management APIs")
public class UnauthorizedUserController {

    @Autowired
    private UnauthorizedUserService unauthorizedUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Operation(summary = "Get all unauthorized user requests", description = "Retrieve a list of all unauthorized user requests.")
    @GetMapping("/getAllRequests")
    public List<UnauthorizedUserWithHistory> getAllRequests() {
        return unauthorizedUserService.getAllRequests();
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users.")
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get all user history", description = "Retrieve a list of all user history entries.")
    @GetMapping("/getAllUserHistory")
    public ResponseEntity<?> getAllUserHistory(){
        return ResponseEntity.ok(historyService.getAllHistoryByEntityType("User"));
    }

    @Operation(summary = "Register a new SuperAdmin", description = "Register a new SuperAdmin with the specified details.")
    @PostMapping("/register")
    public ResponseEntity<?> registerSuperAdmin(@RequestParam String email,
            @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId) {
        System.out.println("Register endpoint hit with email: " + email);
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN".toUpperCase());
        roles.add("USER".toUpperCase());
        roles.add("SUPERADMIN".toUpperCase());
        unauthorizedUserService.registerUser(email, password, roles, talentName, inctureId);
        return ResponseEntity.ok("SuperAdmin registered successfully");
    }

    @Operation(summary = "Approve an unauthorized user request", description = "Approve a request for an unauthorized user by ID.")
    @PutMapping("/approve")
    public ResponseEntity<?> approveRequest(@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        unauthorizedUserService.approveRequest(id, authentication.getName());
        return ResponseEntity.ok("Request approved successfully");
    }

    @Operation(summary = "Decline an unauthorized user request", description = "Decline a request for an unauthorized user by ID.")
    @PutMapping("/decline")
    public ResponseEntity<?> declineRequest(@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        unauthorizedUserService.declineRequest(id, authentication.getName());
        return ResponseEntity.ok("Request declined successfully");
    }

    @Operation(summary = "Change user roles", description = "Change the roles of a user by ID.")
    @PutMapping("/changeRole")
    public ResponseEntity<String> changeRole(@RequestParam Long id, @RequestParam Set<String> newRoles) {
        log.info("Change role hit with id : " + id);
        log.info("New roles : " + newRoles);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(userService.changeRole(id, newRoles, authentication.getName()), HttpStatus.OK);
    }

    @Operation(summary = "Delete users", description = "Delete a list of users.")
    @DeleteMapping(value = "/deleteUsers", consumes = "application/json")
    public ResponseEntity<?> deleteUsers(@RequestBody List<User> users) {
        try {
            userService.deleteUsers(users);
            return ResponseEntity.ok("Users deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users could not be deleted");
        }
    }
}
