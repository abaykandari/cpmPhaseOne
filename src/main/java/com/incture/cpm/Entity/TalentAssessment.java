package com.incture.cpm.Entity;

<<<<<<<< HEAD:src/main/java/com/incture/cpm/Entity/TalentAssessment.java

========
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
>>>>>>>> 437ccfbfd225675ff03b7843f2dc311a7d15ccd4:src/main/java/com/incture/cpm/Entity/Assessment.java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
<<<<<<<< HEAD:src/main/java/com/incture/cpm/Entity/TalentAssessment.java
public class TalentAssessment {
========
>>>>>>>> 437ccfbfd225675ff03b7843f2dc311a7d15ccd4:src/main/java/com/incture/cpm/Entity/Assessment.java

public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelOneId", referencedColumnName = "levelOneId")
    private AssessmentLevelOne assessmentLevelOne;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelTwoId", referencedColumnName = "levelTwoId")
    private AssessmentLevelTwo assessmentLevelTwo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelThreeId", referencedColumnName = "levelThreeId")
    private AssessmentLevelThree assessmentLevelThree;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelFourId", referencedColumnName = "levelFourId")
    private AssessmentLevelFour assessmentLevelFour;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelFiveId", referencedColumnName = "levelFiveId")
    private AssessmentLevelFive assessmentLevelFive;
    
    private double totalScore;

    @PrePersist
    @PreUpdate
    public void updateTotalScore() {
        this.totalScore = (assessmentLevelOne != null ? assessmentLevelOne.getTotalScore() : 0)
                + (assessmentLevelTwo != null ? assessmentLevelTwo.getTotalScore() : 0)
                + (assessmentLevelThree != null ? assessmentLevelThree.getTotalScore() : 0)
                + (assessmentLevelFour != null ? assessmentLevelFour.getTotalScore() : 0)
                + (assessmentLevelFive != null ? assessmentLevelFive.getHrScore() : 0);
    }
}

// totalScore does't calculate the total score properly
