package com.incture.cpm.Repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
    Optional<Candidate> findByCollegeIdAndEmail(int collegeId, String email);

    List<Candidate> findByCollegeId(int collegeId);

    List<Candidate> findByCollegeIdAndEkYear(int collegeId, String ekYear);
    List<Candidate> findByEmailIn(List<String> emails);
}
