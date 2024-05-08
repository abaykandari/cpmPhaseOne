package com.example.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cpm.Entity.Talent;

public interface TalentRepository extends JpaRepository<Talent,Integer>{
    
}
