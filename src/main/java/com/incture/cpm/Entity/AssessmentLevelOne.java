package com.incture.cpm.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

public class AssessmentLevelOne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelOneId;

    private String candidateName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private double quantitativeScore;
    private double logicalScore;
    private double verbalScore;
    private double codingScore;

    private boolean isSelected = false;
}