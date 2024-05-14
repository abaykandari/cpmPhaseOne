package com.incture.cpm.Entity;

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
    private Long talentId;
    private Long candidateId;
    private String collegeName;
    private String talentName;
    private String department;
    private String email;
    private String phoneNumber;
    private String alternateNumber;
    // private String assesmentId;
    // private String interviewId;
    private double tenthPercent;
    private double twelthPercent;

    // @Column(nullable = false)
    // private byte[] marksheetsSemwise;

    private String currentLocation;
    private String permanentAddress;
    private String panNumber;
    private String aadhaarNumber;
    private String fatherName;
    private String motherName;
    private String dob;
    private String talentSkills;
    private String talentEmploymentType;
    private String reportingManager;
    private String plOwner;
    private String ekYear="2024";
    private String talentCategory;//inperson / online
    private double cgpaUndergrad;
    private double cgpaMasters;
    private String officeLocation;

}
