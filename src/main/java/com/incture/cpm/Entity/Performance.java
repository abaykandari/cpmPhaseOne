package com.incture.cpm.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    @Column(nullable = false, unique = true)
    private Long talentId;
    private String talentName;
    private String ekYear;
    private String talentSkills;
    private Long reportingManagerUserId;

    private double assignmentScore;
    private double averageAttendance;
    private double assessmentScore;

    private int punctuality;
    private int technicalProficiency;
    private int proactiveness;
    private int timeliness;

    /*@OneToOne
    @JoinColumn(name = "talentId", referencedColumnName = "talentId", insertable = false, updatable = false)
    private Talent talent; */

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "edit_history_performance_id", referencedColumnName = "performanceId")
    private List<History> editHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "monthly_history_performance_id", referencedColumnName = "performanceId")
    private List<History> monthlyHistory = new ArrayList<>();

    public void setAssessmentScore(double assessmentScore) {
        this.assessmentScore = roundToTwoDecimalPlaces(assessmentScore);
    }

    public void setAssignmentScore(double assignmentScore) {
        this.assignmentScore = roundToTwoDecimalPlaces(assignmentScore);
    }

    public void setAverageAttendance(double averageAttendance) {
        this.averageAttendance = roundToTwoDecimalPlaces(averageAttendance);
    }

    // Utility method to round to two decimal places
    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
