package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.PasswordUpdateReq;
import com.high.highblog.model.entity.Account;
import com.high.highblog.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class AccountCrudBloc {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public AccountCrudBloc(final AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePasswordForCurrentAccount(final PasswordUpdateReq passwordUpdateReq) {
        Long accountId = SecurityHelper.getAccountId();
        log.info("Change password for current account with userId #{}", accountId);
        Account account = accountService.getAccountById(accountId);
        String oldPassword = passwordUpdateReq.getOldPassword();
        String encodedNewPassword = passwordUpdateReq.getNewPassword();

        if (passwordEncoder.matches(oldPassword, account.getPassword())) {
            accountService.savePassword(accountId, encodedNewPassword);
        } else {
            throw new ValidatorException("Incorrect old password", "oldPassword");
        }
    }
}
