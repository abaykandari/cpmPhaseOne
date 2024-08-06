package com.incture.cpm.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "College_TPO")
public class CollegeTPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int collegeId;
    @Column
    private String collegeName;
    @Column
    private String tpoName;
    @Column
    private String primaryEmail;
    @Column
    private String phoneNumber;
    @Column
    private String addressLine1;
    @Column
    private String addressLine2;
    @Column
    private String location;
    @Column
    private String region;
    @Column
    private String collegeOwner;
    @Column
    private String primaryContact;
    @Column
    private String secondaryContact;
    @Column
    private String tier;
    @Column
    private String pinCode;
    @Column
    private String state;
    @Column
    private double compensation;
    @JsonIgnore
    @OneToOne(mappedBy = "collegeTPO", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    private InterviewerScheduling interviewerSchedulings;

        //mapping

    //    @OneToMany(mappedBy = "candidateCollegeId", cascade = CascadeType.ALL)
    //    @JoinColumn(name = "candidateId")
    //    private List<Candidate> candidates;

    @JsonIgnore
    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessment;    

/*     @PrePersist
    public void initializeAssessment() {
        if (this.assessment == null) {
            this.assessment = new Assessment();
            this.assessment.setCollege(this);
        }
    } */
}