package com.high.highblog.service;

import com.high.highblog.model.entity.AccountRole;
import com.high.highblog.repository.AccountRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountRoleService {

    private final AccountRoleRepository accountRoleRepository;

    public AccountRoleService(final AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }

    @Transactional
    public void deleteOldAndSaveNew(final Long accountId, List<Long> roleIds) {
        log.info("Delete old accountRole then save for accountId #{} and list roleIds #{}", accountId, roleIds);
        List<AccountRole> oldAccountRoles = accountRoleRepository.findByAccountId(accountId);
        accountRoleRepository.deleteAll(oldAccountRoles);

        saveNew(accountId, roleIds);
    }

    @Transactional
    public void saveNew(final Long accountId, List<Long> roleIds) {
        log.info("Save for accountId #{} and list roleIds #{}", accountId, roleIds);
        List<AccountRole> accountRoles = roleIds.stream()
                                                .map(roleId -> AccountRole.builder()
                                                                          .accountId(accountId)
                                                                          .role_id(roleId).build())
                                                .collect(Collectors.toList());
        accountRoleRepository.saveAll(accountRoles);
    }
}
