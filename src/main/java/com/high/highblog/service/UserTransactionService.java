package com.high.highblog.service;

import com.high.highblog.enums.UserTransactionStatus;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.repository.UserTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserTransactionService {
    private final UserTransactionRepository repository;

    public UserTransactionService(final UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public UserTransaction getByPaymentId(final String paymentId) {
        log.info("Get by paymentId #{}", paymentId);

        return repository.findByPaymentId(paymentId)
                         .orElseThrow(() -> new ObjectNotFoundException("userTransaction"));
    }

    @Transactional
    public void saveNew(final UserTransaction userTransaction) {
        log.info("Save new userTransaction with data #{}", userTransaction);

        validateBeforeSaveNew(userTransaction);

        repository.save(userTransaction);
    }

    @Transactional
    public void saveStatus(final UserTransaction userTransaction, final UserTransactionStatus status) {
        log.info("Save userTransaction with data #{}", userTransaction);

        validateBeforeSaveStatus(userTransaction);
        userTransaction.setStatus(status);

        repository.save(userTransaction);
    }

    public void validateBeforeSaveNew(final UserTransaction userTransaction) {
        if (ObjectUtils.isNotEmpty(userTransaction.getId())) {
            throw new ValidatorException("Invalid user transaction", "userTransaction");
        }
    }

    public void validateBeforeSaveStatus(final UserTransaction userTransaction) {
        if (ObjectUtils.isEmpty(userTransaction.getId())) {
            throw new ValidatorException("Invalid user transaction", "userTransaction");
        }if(UserTransactionStatus.isFinalStatus(userTransaction.getStatus())){
            throw new ValidatorException("Transaction already completed", "userTransaction");
        }
    }
}
