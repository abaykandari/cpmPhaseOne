package com.incture.cpm.Entity;

import java.sql.Blob;

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

public class AssessmentLevelFinal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelFinalId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;

    private boolean loiSent = false;

    private boolean loiGenerated = false;

    @Column(columnDefinition = "LONGBLOB")
    private Blob loi;
}
