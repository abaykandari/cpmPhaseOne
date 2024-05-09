package com.example.cpm.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cpm.Entity.Regularize;

@Repository
public interface RegularizeRepository extends JpaRepository<Regularize, Integer> {
}
