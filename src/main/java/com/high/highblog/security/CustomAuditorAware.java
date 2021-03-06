package com.high.highblog.security;

import com.high.highblog.constant.Constant;
import com.high.highblog.helper.SecurityHelper;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CustomAuditorAware
        implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityHelper.getAccountUsername().orElse(Constant.SYSTEM_ACCOUNT));
    }
}
