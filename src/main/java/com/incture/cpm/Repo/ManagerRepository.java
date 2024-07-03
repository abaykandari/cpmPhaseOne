package com.incture.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager,Long>{
    
}
