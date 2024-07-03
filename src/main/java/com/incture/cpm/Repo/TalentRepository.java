package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Talent;

@Repository
public interface TalentRepository extends JpaRepository<Talent,Long>{
    Talent findByCandidateId(Long candidateId);
    List<Talent> findByReportingManager(String reportingManager);
    Optional<Talent> findByEmail(String email);
}

