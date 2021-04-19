package com.high.highblog.repository;

import com.high.highblog.model.entity.ThirdPartyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyTransactionRepository extends JpaRepository<ThirdPartyTransaction, Long> {
}
