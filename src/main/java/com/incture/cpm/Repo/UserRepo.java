package com.incture.cpm.Repo;
import com.incture.cpm.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE your_table_name MODIFY COLUMN active BOOLEAN DEFAULT NULL", nativeQuery = true)
    void addDefaultToActiveColumn();

}