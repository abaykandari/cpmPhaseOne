package com.incture.cpm.Entity;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentLevelOne> levelOneList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentLevelTwo> levelTwoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentLevelThree> levelThreeList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentLevelOptional> levelOptionalList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentLevelFinal> levelFinalList = new ArrayList<>();
    
    private Boolean isLevelOneCompleted = false; // once complete, lock the level for further incoming data
    private Boolean isLevelTwoCompleted = false;
    private Boolean isLevelThreeCompleted = false;
    private Boolean isLevelOptionalCompleted = false;

    private String ekYear = String.valueOf(Year.now().getValue()); 
    
    @JsonIgnoreProperties({"tpoName", "primaryEmail", "phoneNumber", "addressLine1", "addressLine2", "location", "region", "collegeOwner", "primaryContact", "secondaryContact", "tier", "pinCode", "state", "compensation"})
    @ManyToOne
    @JoinColumn(name = "collegeId", referencedColumnName = "collegeId")
    private CollegeTPO college;
}
