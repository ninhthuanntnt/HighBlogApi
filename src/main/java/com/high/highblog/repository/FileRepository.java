package com.high.highblog.repository;

import com.high.highblog.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository
        extends JpaRepository<File, Long> {
}
