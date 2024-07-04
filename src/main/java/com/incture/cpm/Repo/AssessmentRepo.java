package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Assessment;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Long>{
    Optional<Assessment> findByEmail(String email);
    Optional<Assessment> findByCollegeId(int collegeId);
}
