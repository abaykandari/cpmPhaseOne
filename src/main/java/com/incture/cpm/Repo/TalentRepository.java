package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Talent;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Talent findByCandidateId(Long candidateId);

    List<Talent> findByReportingManager(String reportingManager);

    Optional<Talent> findByEmail(String email);

    // For Summary of Talent table

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T")
    // int countTotalTalents();

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T WHERE
    // T.talent_status='ACTIVE'")
    // int countActiveTalents();

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T WHERE
    // T.talent_status<>'ACTIVE'")
    // int countInactiveTalents();

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T WHERE
    // T.talent_status='DECLINED'")
    // int countDeclinedTalents();

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T WHERE
    // T.talent_status='RESIGNED'")
    // int countResignedTalents();

    // @Query(value = "SELECT COUNT(T.talent_id) FROM talent T WHERE
    // T.talent_status='REVOKED'")
    // int countRevokedTalents();
}
