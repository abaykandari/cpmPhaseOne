package com.incture.cpm.Service;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.User;
import com.incture.cpm.Repo.TalentRepository;
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

    public User registerUser(String email, String password, Set<String> roles, String talentName, String inctureId) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(!existingUser.isEmpty()) throw new BadCredentialsException("User already exists for the given email");
        
        Optional<Talent> talentOptional = talentRepository.findByEmail(email);
        //if (talentOptional.isEmpty())   throw new BadCredentialsException("No talent info found for the given user");

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if(talentOptional.isPresent()) user.setTalentId(talentOptional.get().getTalentId());

        Set<String> prefixedRoles = roles.stream()
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .collect(Collectors.toSet());
       
        user.setRoles(prefixedRoles);
        user.setInctureId(inctureId);
        user.setTalentName(talentName);
        return userRepository.save(user);
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
}