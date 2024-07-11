package com.incture.cpm.Service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.UnauthorizedUser;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.Repo.UnauthorizedUserRepo;
import com.incture.cpm.Repo.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
 
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TalentRepository talentRepository;
    @Autowired
    private UnauthorizedUserRepo unauthorizedUserRepo; 
    
    @Transactional
    public String registerUser(String email, String password, Set<String> roles, String talentName, String inctureId) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) throw new BadCredentialsException("User already exists for the given email");
        /* Set<String> prefixedRoles = roles.stream()
                                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                                        .collect(Collectors.toSet()); */

        Optional<Talent> talentOptional = talentRepository.findByEmail(email);
        if (email.endsWith("@incture.com") || talentOptional.isPresent()){       
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setTalentId(talentOptional.get().getTalentId());
            //user.setRoles(prefixedRoles);
            user.setRoles(roles);
            user.setInctureId(inctureId);
            user.setTalentName(talentName);
            userRepository.save(user);

            return "User";
        } else{
            Optional<UnauthorizedUser> existingUnauthorizedUser = unauthorizedUserRepo.findByEmail(email);
            if(existingUnauthorizedUser.isPresent()) throw new BadCredentialsException("User already exists for the given email");
            UnauthorizedUser newUser = new UnauthorizedUser();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRoles(roles);
            //newUser.setRoles(prefixedRoles);
            newUser.setInctureId(inctureId);
            newUser.setTalentName(talentName);
            newUser.setStatus("Pending");
            unauthorizedUserRepo.save(newUser);

            return "UnauthorizedUser";
        }
    }

    public void printUserRoles(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Roles for user " + email + ": " + user.getRoles());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public void addRole(String email, String newRole) {
        User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getRoles().add(newRole);

        Set<String> prefixedRoles = user.getRoles().stream()
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .collect(Collectors.toSet());
       
        user.setRoles(prefixedRoles);
        userRepository.save(user);
    }

    public ResponseEntity<String> changePassword(String email, String password) {
        User user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }
}