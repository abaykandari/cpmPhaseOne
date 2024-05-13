package com.incture.cpm.Service;


import com.incture.cpm.Entity.User;
import com.incture.cpm.Repo.UserRepo;


//import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public String register(User user) {
        this.userRepo.save(user);
        return "registration successful";
    }


//    @Autowired
//    private OtpUtil otpUtil;
//    @Autowired
//    private EmailUtil emailUtil;
//
//    public String register(Registerdto registerDto) {
//        Optional<User> savedUser=userRepo.findByEmail(registerDto.getEmail());
//        if(!savedUser.isEmpty()){
//            throw new RuntimeException("User alreday exist");
//        }
//        else{
//
//        String otp = otpUtil.generateOtp();
//        try {
//            System.out.println("testing 1");
//            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
//            System.out.println("testing 2");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }
//        User user = new User();
//        user.setUserId(generateRandomInt());
//        user.setName(registerDto.getName());
//        user.setEmail(registerDto.getEmail());
//        user.setPassword(registerDto.getPassword());
//        user.setOtp(otp);
//        user.setOtpGeneratedTime(LocalDateTime.now());
//        user.setMobileNumber(registerDto.getMobileNumber());
//        user.setActive(true);
//        userRepo.save(user);
//        return "User registration successful";
//    }
//}
//
//    public String verifyAccount(String email, String otp) {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
//                LocalDateTime.now()).getSeconds() < (5 * 60)) {
//            user.setActive(true);
//            userRepo.save(user);
//            return "OTP verified you can login";
//        }
//        return "Please regenerate otp and try again";
//    }
//
//
//    public String regenerateOtp(String email) {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
//        String otp = otpUtil.generateOtp();
//        try {
//            emailUtil.sendOtpEmail(email, otp);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Unable to send otp please try again");
//        }
//        user.setOtp(otp);
//        user.setOtpGeneratedTime(LocalDateTime.now());
//        userRepo.save(user);
//        return "Email sent... please verify account within 1 minute";
//    }
//
//
//    public String login(Logindto loginDto) {
//        User user = userRepo.findByEmail(loginDto.getEmail())
//                .orElseThrow(
//                        () -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
//        if (!loginDto.getPassword().equals(user.getPassword())) {
//            return "Password is incorrect";
//        } else if (!user.isActive()) {
//            return "your account is not verified";
//        }
//        return "Login successful";
//    }
//
//
    public List<User> khoj() {
        return this.userRepo.findAll();
    }
//
    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }

    public String khatam() {
        this.userRepo.deleteAll();
        return  "deleted Successfully";
    }
}

