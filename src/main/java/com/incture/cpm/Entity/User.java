package com.incture.cpm.Entity;

import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column
    private  String name;
    private String email;
    private String password;
    @JsonIgnore
    private  String role;

    private boolean adminCheck=true;
//    private String role;
}
 