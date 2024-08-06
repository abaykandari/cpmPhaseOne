package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.AssessmentLevelTwo;

public interface AssessmentLevelTwoRepo extends JpaRepository<AssessmentLevelTwo, Long>{
    Optional<AssessmentLevelTwo> findByEmail(String email);
}
