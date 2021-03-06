package com.high.highblog.service;

import com.high.highblog.constant.Constant;
import com.high.highblog.enums.CodeType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.ConfirmationCode;
import com.high.highblog.repository.ConfirmationCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

@Slf4j
@Service
public class ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;
    private static final Long DEFAULT_EXPIRATION = Constant.FIVE_MINUTE_EXPIRATION;

    public ConfirmationCodeService(final ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ConfirmationCode getByIdAndCodeAndType(final Long id, final String code, final CodeType codeType) {
        log.info("Get confirmation code by id #{} and code #{} and codeType #{}", id, code, codeType);

        return repository.findByIdAndCodeAndCodeType(id, code, codeType)
                         .orElseThrow(() -> new ObjectNotFoundException("confirmationCode"));
    }

    public void save(ConfirmationCode confirmationCode) {
        log.info("Save confirmation code with data #{}", confirmationCode);

        repository.save(confirmationCode);
    }

    @Transactional
    public ConfirmationCode save(final Long accountId, final String code, final CodeType codeType) {
        log.info("Create confirmation code with code #{} for type #{}", code, codeType.name());
        ConfirmationCode confirmationCode = ConfirmationCode.builder()
                                                            .accountId(accountId)
                                                            .code(code)
                                                            .codeType(codeType)
                                                            .expiration(DEFAULT_EXPIRATION)
                                                            .build();
        this.save(confirmationCode);
        return confirmationCode;
    }
}
