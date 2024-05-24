package com.incture.cpm.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localKey;

    private Long talentId;
    private Long assessmentId;
    private String assessmentType;
    private String assessmentSkill;
    private String location;
    private double score;
    private int attempts;
    private String comments;
    private String assessmentDate;

    
}
