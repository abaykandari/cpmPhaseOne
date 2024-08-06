package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.AssessmentLevelFinal;

public interface AssessmentLevelFinalRepo extends JpaRepository<AssessmentLevelFinal, Long>{
    Optional<AssessmentLevelFinal> findByEmail(String email);
}
