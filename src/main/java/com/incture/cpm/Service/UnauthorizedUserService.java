package com.incture.cpm.Service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.cpm.Entity.UnauthorizedUser;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Repo.UnauthorizedUserRepo;
import com.incture.cpm.Repo.UserRepository;

@Service
public class UnauthorizedUserService {

    @Autowired
    private UnauthorizedUserRepo unauthorizedUserRepository; 
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public List<UnauthorizedUser> getAllRequests() {
        return unauthorizedUserRepository.findAll();
    }
    
    @Transactional
    public void approveRequest(Long id) {
        try{
            UnauthorizedUser unauthorizedUser = unauthorizedUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User does not exists"));
            
            User newUser = new User(unauthorizedUser);
            userRepository.save(newUser);

            unauthorizedUser.setStatus("Approved");
            unauthorizedUserRepository.save(unauthorizedUser);
            sendStatusEmail(unauthorizedUser.getEmail(), "approved");
            //
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while approving the request.", e);
        }
    }

    private void sendStatusEmail(String email, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CPM -> Your OTP Code");
        if(status == "approved") message.setText("Your Registration request is approved :)");
        else message.setText("Your Registration request is declined :(");
        mailSender.send(message);
    }
    
    public void declineRequest(Long id) {
        try{
            UnauthorizedUser unauthorizedUser = unauthorizedUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User does not exists"));
        
            unauthorizedUser.setStatus("Declined");
            unauthorizedUserRepository.save(unauthorizedUser);
            sendStatusEmail(unauthorizedUser.getEmail(), "declined");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while approving the request.", e);
        }
    }

    public void registerUser(String email, String password, Set<String> roles, String talentName, String inctureId) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        /* Set<String> prefixedRoles = roles.stream()
                                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                                .collect(Collectors.toSet()); */

        user.setRoles(roles);
        user.setInctureId(inctureId);
        user.setTalentName(talentName);
        userRepository.save(user);
    }
}