package com.incture.cpm.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academicinterns")
public class AcademicInterns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "date")
    private String date;
    @Column(name = "join_time")
    private String joinTime;
    @Column(name = "leave_time")
    private String leaveTime;
    @Column(name = "duration")
    private Duration duration;
    @Column(name = "duration_minutes")
    private Long durationMinutes;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    @Column(name = "participant_id")
    private String participantId;
    private String status; // Present, Absent
    // Constructors, getters, and setters
}