package com.high.highblog.security;

import com.high.highblog.enums.RoleType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.Account;
import com.high.highblog.model.entity.Role;
import com.high.highblog.repository.AccountRepository;
import com.high.highblog.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService
    implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(final AccountRepository accountRepository,
                                    final RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        return accountRepository.findByUsername(username)
                                .map(this::buildSpringSecurityUser)
                                .orElseThrow(() -> new ObjectNotFoundException("account"));
    }

    private CustomUserDetails buildSpringSecurityUser(final Account account) {
        Set<Role> roles = new HashSet<>(roleRepository.fetchByAccountId(account.getId()));

        for (Role role : roles) {
            if (role.getRoleType() == RoleType.ROLE_LOCKED_USER) {
                throw new ValidatorException("Locked account", "account");
            }
        }

        Set<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                                                              .map(role -> new SimpleGrantedAuthority(role.getRoleType()
                                                                                                          .name()))
                                                              .collect(Collectors.toSet());

        return new CustomUserDetails(account.getId(),
                                     account.getUserId(),
                                     account.getUsername(),
                                     account.getPassword(),
                                     grantedAuthorities);
    }
}
