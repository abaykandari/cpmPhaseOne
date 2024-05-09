package com.example.cpm.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter

public class Regularize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regularizeId;
    
    private String talentName;
    private String checkin;
    private String checkout;
    private String approvalManager;
    private String action;
    private String attendanceDate;
    private String regularizeDate;
    private int talentId;
}

