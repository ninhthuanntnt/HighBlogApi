package com.high.highblog.repository;

import com.high.highblog.model.entity.SystemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemTransactionRepository extends JpaRepository<SystemTransaction, Long> {
}