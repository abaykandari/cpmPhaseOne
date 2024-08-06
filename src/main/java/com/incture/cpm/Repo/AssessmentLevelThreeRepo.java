package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.AssessmentLevelThree;

public interface AssessmentLevelThreeRepo extends JpaRepository<AssessmentLevelThree, Long>{
    Optional<AssessmentLevelThree> findByEmail(String email);
}
