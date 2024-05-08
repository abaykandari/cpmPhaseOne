package com.example.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cpm.Entity.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Integer>{
    
}
