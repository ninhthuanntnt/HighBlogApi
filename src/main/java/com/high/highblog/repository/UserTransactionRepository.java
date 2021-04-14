package com.high.highblog.repository;

import com.high.highblog.model.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTransactionRepository
        extends JpaRepository<UserTransaction, Long> {

    @Query("SELECT DISTINCT ut FROM UserTransaction ut"
            + " JOIN SystemTransaction st ON st.senderTransactionId = ut.id"
            + " JOIN ThirdPartyTransaction  tpp ON tpp.id = st.thirdPartyTransactionId"
            + " WHERE tpp.paymentId = :paymentId")
    Optional<UserTransaction> findByPaymentId(String paymentId);
}
