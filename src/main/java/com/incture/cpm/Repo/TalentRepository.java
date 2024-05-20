package com.incture.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Talent;

public interface TalentRepository extends JpaRepository<Talent,Long>{
    Talent findByCandidateId(Long CandidateId); 
}
