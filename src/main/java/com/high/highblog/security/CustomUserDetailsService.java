package com.high.highblog.security;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.Account;
import com.high.highblog.repository.AccountRepository;
import com.high.highblog.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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
        Set<GrantedAuthority> grantedAuthorities =
                roleRepository.fetchByAccountId(account.getId())
                              .stream()
                              .map(role -> new SimpleGrantedAuthority(role.getRoleType().name()))
                              .collect(Collectors.toSet());

        return new CustomUserDetails(account.getId(),
                                     account.getUserId(),
                                     account.getUsername(),
                                     account.getPassword(),
                                     grantedAuthorities);
    }
}
