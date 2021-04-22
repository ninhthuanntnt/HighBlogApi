package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Wallet;
import com.high.highblog.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class WalletService {

    private WalletRepository repository;

    public WalletService(final WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Wallet getToSaveByUserId(final Long userId) {
        log.info("Get to save wallet by userId #{}", userId);

        return repository.findToSaveByUserId(userId)
                         .orElseThrow(() -> new ObjectNotFoundException("userWallet"));
    }

    @Transactional
    public void saveBalance(final Long userId, final BigDecimal newBalance) {
        log.info("Save user wallet by userId #{} and newBalance #{}", userId, newBalance);

        Wallet wallet = getToSaveByUserId(userId);

        validateBeforeSaveBalance(wallet);

        wallet.setBalance(newBalance);

        repository.save(wallet);
    }

    @Transactional
    public void addBalance(final Long userId, final BigDecimal amount){
        log.info("Add balance in wallet by userIId #{} with amount #{}", userId, amount);

        Wallet wallet = getToSaveByUserId(userId);

        BigDecimal newBalance = wallet.getBalance()
                                      .add(amount);
        wallet.setBalance(newBalance);

        repository.save(wallet);
    }

    @Transactional
    public void subtractBalanceIfSufficient(final Long userId, final BigDecimal amount) {
        log.info("Subtract balance in user wallet if sufficent by userId #{} and amount #{}", userId, amount);

        Wallet wallet = getToSaveByUserId(userId);
        BigDecimal newBalance = wallet.getBalance()
                                      .subtract(amount);
        wallet.setBalance(newBalance);

        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new ValidatorException("Insufficient money", "wallet");
        }

        repository.save(wallet);
    }

    public void validateBeforeSaveBalance(final Wallet wallet) {
        if (ObjectUtils.isEmpty(wallet.getId())) {
            throw new ValidatorException("Invalid wallet", "wallet");
        }
    }
    @Transactional(readOnly = true)
    public Wallet getBalance(final long userId){
        return repository.findByUserId(userId).orElseThrow(() -> new ObjectNotFoundException("userWallet"));
    }

}
