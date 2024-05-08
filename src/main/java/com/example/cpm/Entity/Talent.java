package com.example.cpm.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Talent {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_id")
    private int talentId;

    private int candidateId;
    private int collegeId;
    private String talentName;
    private String department;
    private String email;
    private String phoneNumber;
    private String alternateNumber;
    private String assesmentId;
    private String interviewId;
    private float tenthPercent;
    private float twelthPercent;
    private String marksheetsSemwise;
 
    private String currentLocation;
    private String permanentAddress;
    private String panNumber;
    private String aadharNumber;
    private String fatherName;
    private String motherName;
    private Date dob;
    private String talentSkills;
    private String talentEmoymentType;
    private String reportingManager;
    private String plOwner;
    private String ekYear;
    private String talentCategory;
}
 