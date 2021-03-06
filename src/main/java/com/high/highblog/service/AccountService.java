package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.Account;
import com.high.highblog.repository.AccountRepository;
import com.high.highblog.repository.AccountRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(final AccountRepository accountRepository,
                          final PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean isExistAccountByUsername(final String username) {
        log.info("Check whether exists account with username #{}", username);

        return accountRepository.findByUsername(username)
                                .isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isExistAccountByEmail(final String email) {
        log.info("Check whether exists account with email #{}", email);

        return accountRepository.findByUsername(email)
                                .isPresent();
    }

    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        log.info("Get account by id #{}", getAccountById(id));

        return accountRepository.findById(id)
                                .orElseThrow(() -> new ObjectNotFoundException("account"));
    }

    @Transactional
    public void save(final Account account) {
        log.info("Save account with info #{}", account);

        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        accountRepository.save(account);
    }
}
