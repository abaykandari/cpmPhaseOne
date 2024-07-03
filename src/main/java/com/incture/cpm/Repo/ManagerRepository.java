package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Entity.Team;

public interface ManagerRepository extends JpaRepository<Manager,Long>{

    List<Manager> findByTeam(Team team);

    Optional<Manager> findByTalentId(Long talentId);
    
}
