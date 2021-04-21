package com.high.highblog.repository;

import com.high.highblog.model.entity.User;
import com.high.highblog.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findToSaveByUserId(Long userId);

    Optional<Wallet> findByUserId(Long userId);
}
