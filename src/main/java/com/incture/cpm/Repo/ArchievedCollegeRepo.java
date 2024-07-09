package com.incture.cpm.Repo;

import com.incture.cpm.Entity.ArchievedColleges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchievedCollegeRepo extends JpaRepository <ArchievedColleges, Integer>{
}
