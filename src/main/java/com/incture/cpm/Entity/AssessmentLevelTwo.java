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

public class AssessmentLevelTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelTwoId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;
    
    //10 marks each
    private double problemStatement; 
    private double processWorkflow;
    private double useOfAlgorithms;

    private double techStacks;
    private double recommendedSolution;

    private double languageAndGrammar;
    private double logicalFlow;

    private boolean isSelected = false;
}
