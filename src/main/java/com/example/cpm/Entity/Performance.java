package com.example.cpm.Entity;


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
    private int talentId;

    private String talentName;
    private String ekYear;
    private int assignmentScore;
    private int averageAttendance;
    private int assessmentScore;
    private int attendance;
    private int punctuality;
    private int technicalProficiency;
    private int proactiveness;
    private int timeliness;
    private String talentSkills;
}
