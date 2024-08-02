package com.incture.cpm.Entity;

import java.sql.Blob;

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
    private Long talentId;
    private Long candidateId;
    private String collegeName;
    private String talentName;
    private String department;

    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private String alternateNumber;
    private double tenthPercent=0.0;
    private double twelthPercent=0.0;

    private Blob marksheetsSemwise;

    private String currentLocation;
    private String permanentAddress;
    private String panNumber;
    private String aadhaarNumber;
    private String fatherName;
    private String motherName;
    private String dob;
    private String talentSkills;
    private String talentEmploymentType;// intern/associate/soft engn. / product manager/ and others
    private String reportingManager;
    private String plOwner;
    private String ekYear;
    private String talentCategory;// inperson / online
    private double cgpaUndergrad=0.0;
    private double cgpaMasters=0.0;
    private String officeLocation;

    // new field added
    private Blob resume;

    // for resignation purpose
    private String talentStatus = "ACTIVE";
    private String exitDate;
    private String exitReason = "NA";
    private String exitComment;

/*     @OneToOne(mappedBy = "talent")
    private Performance performance; */
}
