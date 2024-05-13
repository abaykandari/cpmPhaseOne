package com.incture.cpm.Entity;

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

public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long talentId;
    private String talentName;
    private String talentCategory;
    private String officeLocation;
    private String ekYear;
    private String status;
    
    private String date;
    private String checkin;
    private String checkout;

    public String totalHours;
}
