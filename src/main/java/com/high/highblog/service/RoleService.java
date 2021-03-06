package com.high.highblog.service;

import com.high.highblog.enums.RoleType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.Role;
import com.high.highblog.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> fetchRolesByAccountId(Long accountId) {
        return roleRepository.fetchByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public Role getRoleByRoleType(final RoleType roleType) {
        log.info("Get role by type #{}", roleType.name());

        return roleRepository.findByRoleType(roleType)
                             .orElseThrow(() -> new ObjectNotFoundException("role"));
    }

    @Transactional
    public List<Role> fetchByAccountId(final Long accountId) {
        log.info("Fetch roles by account id #{}", accountId);

        return roleRepository.fetchByAccountId(accountId);
    }
}
