package com.high.highblog.repository;

import com.high.highblog.model.entity.SystemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemTransactionRepository extends JpaRepository<SystemTransaction, Long> {

    @Query("SELECT new SystemTransaction(st,tpt) FROM SystemTransaction st "
            + " JOIN ThirdPartyTransaction tpt ON tpt.id = st.thirdPartyTransactionId"
            + " WHERE st.senderTransactionId = :senderId")
    List<SystemTransaction> fetchBySenderTransactionId(Long senderId);

    boolean existsByReceiverTransactionId(Long receiverId);
}
