package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Authreq;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Repo.UserRepo;
import com.incture.cpm.Service.UserService;
//import com.example.CampusCalendar.dto.Logindto;
//import com.example.CampusCalendar.dto.Registerdto;
import com.incture.cpm.dto.Logindto;
//import com.example.CampusCalendar.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @PostMapping("/register")
    public  ResponseEntity<String> funReg(@RequestBody User user){
        return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> logCheck(@RequestBody Authreq authreq){
        String email=authreq.getEmail();
        String password=authreq.getPassword();
        User user=userRepo.findByEmail(email);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");

        }
        if(!user.getPassword().equals(password)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");

        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // If authentication is successful, you can return a success response or generate a token
            return ResponseEntity.ok(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> authenticateUser(@RequestBody Logindto loginRequest) {
//        try{
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//        );
//
//        // If authentication is successful, generate and return a JWT token
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String token = jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(token);
//    }
//    catch (
//    BadCredentialsException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
//    }
//    }
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody Registerdto registerDto) {
//        return new ResponseEntity<>(userService.register(registerDto), HttpStatus.OK);
//    }
    @GetMapping("/viewdata")
        public List<User> viewFunction () {
            return userService.khoj();
        }
//
//    @PutMapping("/verify-account")
//    public ResponseEntity<String> verifyAccount(@RequestParam String email,
//                                                @RequestParam String otp) {
//        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
//    }
//
//    @PutMapping("/regenerate-otp")
//    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
//        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
//    }
//    @PutMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Logindto loginDto) {
//        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
//    }
    @DeleteMapping("/deleteAll")
    public String temp(){
        return userService.khatam();
    }
}
