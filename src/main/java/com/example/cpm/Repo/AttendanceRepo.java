package com.example.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cpm.Entity.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Integer>{
    Optional<Attendance> findByTalentIdAndDate(int talentId, String date);
    Optional<List<Attendance>> findByDate(String date);
}
