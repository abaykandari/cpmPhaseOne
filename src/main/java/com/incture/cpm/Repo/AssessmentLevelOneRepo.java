package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.AssessmentLevelOne;

public interface AssessmentLevelOneRepo extends JpaRepository<AssessmentLevelOne, Long>{
    Optional<AssessmentLevelOne> findByEmail(String email);
}
