package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.incture.cpm.Entity.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment,Long>{
    Optional<List<Assessment>> findAllByTalentId(Long talentId);
}
