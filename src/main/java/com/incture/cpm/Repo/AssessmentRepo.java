package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Dto.AssessmentProjection;
import com.incture.cpm.Entity.Assessment;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Long>{
    @Query("SELECT a FROM Assessment a WHERE a.college.collegeId = :collegeId AND a.ekYear = :ekYear")
    Optional<Assessment> findByCollegeIdAndEkYear(int collegeId, String ekYear);

    @Query("SELECT a.assessmentId AS assessmentId, a.ekYear AS ekYear, a.isLevelOneCompleted AS isLevelOneCompleted, a.isLevelTwoCompleted AS isLevelTwoCompleted, a.isLevelThreeCompleted AS isLevelThreeCompleted, a.isLevelOptionalCompleted AS isLevelOptionalCompleted, a.college.collegeId AS collegeId, a.college.collegeName AS collegeName FROM Assessment a WHERE a.ekYear = :ekYear")
    List<AssessmentProjection> findByEkYear(String ekYear);
}
