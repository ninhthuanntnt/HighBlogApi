package com.high.highblog.repository;

import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTransactionRepository
        extends JpaRepository<UserTransaction, Long> {

    @Query("SELECT DISTINCT ut FROM UserTransaction ut"
            + " JOIN SystemTransaction st ON st.senderTransactionId = ut.id"
            + " JOIN ThirdPartyTransaction  tpp ON tpp.id = st.thirdPartyTransactionId"
            + " WHERE tpp.paymentId = :paymentId")
    Optional<UserTransaction> findByPaymentId(String paymentId);

    Page<UserTransaction> findAllByUserId(Long userId, Pageable pageable);
}
