package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByFileName(String string);

}
