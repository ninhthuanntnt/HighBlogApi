package com.high.highblog.repository;

import com.high.highblog.model.entity.ThirdPartyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThirdPartyTransactionRepository extends JpaRepository<ThirdPartyTransaction, Long> {

    Optional<ThirdPartyTransaction> findFirstByPaymentIdOrderByIdDesc(String paymentId);
}
