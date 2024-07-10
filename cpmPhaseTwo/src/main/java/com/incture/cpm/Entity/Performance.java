package com.incture.cpm.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @Column(name = "talent_id")
    private Long talentId;

    private String talentName;
    private String ekYear;
    private String talentSkills;

    private double assignmentScore;
    private double averageAttendance;
    private double assessmentScore;
    private int punctuality;
    private int technicalProficiency;
    private int proactiveness;
    private int timeliness;
}
