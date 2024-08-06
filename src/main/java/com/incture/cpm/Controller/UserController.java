package com.incture.cpm.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.User;
import com.incture.cpm.Service.CustomUserDetailsService;
import com.incture.cpm.Service.OtpService;
import com.incture.cpm.Service.UserService;
import com.incture.cpm.Util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/security")
@Slf4j
@Tag(name = "User", description = "User management APIs")
public class UserController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Home endpoint", description = "Returns a welcome message for the home page.")
    @GetMapping("/")
    public String home() {
        return "Welcome to the home page!";
    }

    @Operation(summary = "Get user details", description = "Retrieve the details of the currently authenticated user.")
    @GetMapping("/user")
    public ResponseEntity<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @Operation(summary = "Admin access endpoint", description = "Returns a message for admin access.")
    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin Content.";
    }

    @Operation(summary = "Get OTP store", description = "Retrieve the OTP store.")
    @GetMapping("/allOtp")
    public Map<String, String> getOtpStore() {
        return otpService.getOtpStore();
    }

    @Operation(summary = "Generate OTP", description = "Generate an OTP and send it to the specified email.")
    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        otpService.generateOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @Operation(summary = "Forgot password", description = "Reset the password using the provided email, password, and OTP.")
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email, @RequestParam String password, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return userService.changePassword(email, password);
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @Operation(summary = "Register user", description = "Register a new user with the provided details and OTP verification.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String email,
            @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId,
            @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        
        if (isValid) {
            Set<String> roles = new HashSet<>();
            roles.add("USER");

            String message = userService.registerUser(email, password, roles, talentName, inctureId);
            if("User".equals(message)) {
                return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
            } else if("UnauthorizedUser".equals(message)) {
                return new ResponseEntity<>("User registration is pending approval", HttpStatus.I_AM_A_TEAPOT);
            } else {
                return new ResponseEntity<>("User not registered", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @Operation(summary = "Login", description = "Authenticate the user and return a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);

            log.info("Login successful with jwt : {}", jwt);
            return new ResponseEntity<>(userService.findByEmail(email), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Register admin", description = "Register a new admin with the provided details.")
    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestParam String email,
            @RequestParam String password, @RequestParam String talentName, @RequestParam String inctureId) {
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN".toUpperCase());
        roles.add("USER".toUpperCase());
        userService.registerUser(email, password, roles, talentName, inctureId);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @Operation(summary = "Change user password", description = "Change the password of the currently authenticated user.")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String password) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            userService.changePassword(email, password);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID. Ensure only admins can perform this operation.")
    @DeleteMapping("/admin/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or could not be deleted");
        }
    }

    @Operation(summary = "Upload user photo", description = "Upload a photo for a user by ID.")
    @PutMapping("/photo/{id}")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        try {
            userService.saveUserPhoto(id, photo);
            return new ResponseEntity<>("Photo uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload photo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get user photo", description = "Retrieve the photo of a user by ID.")
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable Long id) {
        byte[] photo = userService.getUserPhoto(id);
        String contentType = userService.getUserPhotoContentType(id);
        if (photo != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
