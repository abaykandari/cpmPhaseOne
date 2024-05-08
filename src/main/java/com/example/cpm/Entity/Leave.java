/* package com.example.cpm.Entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveId;

    private String approvalStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Temporal(TemporalType.DATE)
    private Date startDate;

    private int averageManagerFeedback;

    private String approverName;

    @ManyToOne
    @JoinColumn(name = "talentId", nullable = false)
    private Talent talent;

    @PrePersist
    protected void onCreate() {
        this.date = new Date();
    }
}
 */