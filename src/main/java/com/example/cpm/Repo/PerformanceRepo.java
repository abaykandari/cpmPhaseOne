package com.example.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cpm.Entity.Performance;

@Repository
public interface PerformanceRepo extends JpaRepository<Performance, Integer>{
    
}
