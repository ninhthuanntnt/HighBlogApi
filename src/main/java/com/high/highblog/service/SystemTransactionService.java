package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.SystemTransaction;
import com.high.highblog.repository.SystemTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SystemTransactionService {

    private SystemTransactionRepository repository;

    public SystemTransactionService(final SystemTransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveNew(final SystemTransaction systemTransaction) {
        log.info("Save new systemTransaction with data #{}", systemTransaction);

        validateBeforeSaveNew(systemTransaction);

        repository.save(systemTransaction);
    }

    public void validateBeforeSaveNew(final SystemTransaction systemTransaction) {
        if (ObjectUtils.isNotEmpty(systemTransaction.getId())) {
            throw new ValidatorException("Invalid system transaction", "systemTransaction");
        }
    }
}
