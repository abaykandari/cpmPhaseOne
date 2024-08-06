package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.AssessmentLevelOptional;

public interface AssessmentLevelOptionalRepo extends JpaRepository<AssessmentLevelOptional, Long>{
    Optional<AssessmentLevelOptional> findByEmail(String email);
}
