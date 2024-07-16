package com.incture.cpm.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Dto.UnauthorizedUserWithHistory;
import com.incture.cpm.Service.UnauthorizedUserService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin("*")
@RequestMapping("/super/security")
public class UnauthorizedUserController {

    @Autowired
    private UnauthorizedUserService unauthorizedUserService;

    @GetMapping("/getAllRequests")
    public List<UnauthorizedUserWithHistory> getAllRequests() {
        return unauthorizedUserService.getAllRequests();
    }

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

    @PutMapping("/approve")
    public ResponseEntity<?> approveRequest(@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        unauthorizedUserService.approveRequest(id, authentication.getName());
        return ResponseEntity.ok("Request approved successfully");
    }

    @PutMapping("/decline")
    public ResponseEntity<?> declineRequest(@RequestParam Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        unauthorizedUserService.declineRequest(id, authentication.getName());
        return ResponseEntity.ok("Request declined successfully");
    }
}
