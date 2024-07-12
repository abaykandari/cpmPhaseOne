package com.incture.cpm.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateOfTraining;
    private String actualDateOfTraining;
    private String startTime;
    private String endTime;
    private String trainingTech; //topic
    private String trainingTopic; //subtopic
    private String trainerName;
    private String weekNumber;
    private String trainingStatus;
    private String comments;
    private Long trainerId; //trainer
    private List<Long> talentId; //ek year
    
}
