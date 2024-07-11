package com.incture.cpm.Controller;

import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Dto.UserDto;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Service.OtpService;
import com.incture.cpm.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
 
@RestController
@CrossOrigin("*")
@RequestMapping("/security")
public class UserController {
 
    @Autowired
    private OtpService otpService;
 
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String home() {
        return "Welcome to the home page!";
    }
 
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication object: " + authentication);
        if (authentication != null) {
            System.out.println("Is authenticated: " + authentication.isAuthenticated());
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Authorities: " + authentication.getAuthorities());
        }
        
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(getUserDetails(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
 
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Content.";
    }

    @GetMapping("/allOtp")
    public Map<String, String> getOtpStore() {
        return otpService.getOtpStore();
    }

    @PostMapping("/generateOtp") //from /register
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        otpService.generateOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email, @RequestParam String password, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return userService.changePassword(email, password);
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }   
    }
    
    @PostMapping("/register") // registration with otp verification
    public ResponseEntity<?> registerUser(@RequestParam String email,
                                          @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId, @RequestParam String otp) {
        System.out.println("Register endpoint hit with email: " + email);
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            String role = "USER";
            Set<String> roles = new HashSet<>();
            roles.add(role.toUpperCase());
            String message = userService.registerUser(email, password, roles, talentName, inctureId);
            if(message == "User")
                return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
            else if(message == "UnauthorizedUser")
                return new ResponseEntity<>("User registered successfully", HttpStatus.I_AM_A_TEAPOT);
            return new ResponseEntity<>("User non registered", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestParam String email,
                                          @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId) {
        System.out.println("Register endpoint hit with email: " + email);
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN".toUpperCase());
        roles.add("USER".toUpperCase());
        userService.registerUser(email, password, roles, talentName, inctureId);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRole(@RequestParam String email, @RequestParam String role){
        try { 
            userService.addRole(email, role);
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String password){
        try { 
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            userService.changePassword(email, password);
            return ResponseEntity.ok("Role added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/returnUser")
    @PreAuthorize("hasRole('USER')")
    public UserDto getUserDetails(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        userDto.setTalentId(user.getTalentId());
        userDto.setTalentName(user.getTalentName());
        userDto.setInctureId(user.getInctureId());
        
        // Set other necessary fields
        return userDto;
    }

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String auth = email + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        
        User user = userService.findByEmail(email);
        return ResponseEntity.ok()
                .headers(headers)
                .body(getUserDetails(user));
    } 

    /* public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            System.out.println("User authenticated: " + authentication.isAuthenticated());
            System.out.println("Authentication principal: " + authentication.getPrincipal());
            
            User user = userService.findByEmail(email);
            return new ResponseEntity<>(getUserDetails(user), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    } */

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')") 
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, null, null);
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Ensure only admins can delete users
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or could not be deleted");
            }
    }
}