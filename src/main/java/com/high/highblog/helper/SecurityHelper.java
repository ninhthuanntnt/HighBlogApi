package com.high.highblog.helper;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityHelper {

    private SecurityHelper() {
    }

    public static Optional<String> getAccountUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                       .map(authentication -> {
                           if (authentication.getPrincipal() instanceof UserDetails) {
                               UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                               return springSecurityUser.getUsername();
                           } else if (authentication.getPrincipal() instanceof String) {
                               return (String) authentication.getPrincipal();
                           }
                           return null;
                       });
    }

    public static Long getAccountId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return customUserDetails.getId();
        }
        return null;
    }

    public static Long getUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.of(securityContext.getAuthentication())
                       .map(authentication -> {
                           if (authentication.getPrincipal() instanceof UserDetails) {
                               CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                               return customUserDetails.getUserId();
                           }
                           return null;
                       }).orElseThrow(() -> new ObjectNotFoundException("userId"));
    }

    public static Long getNullableUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.of(securityContext.getAuthentication())
                       .map(authentication -> {
                           if (authentication.getPrincipal() instanceof UserDetails) {
                               CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                               return customUserDetails.getUserId();
                           }
                           return null;
                       }).orElse(null);
    }

    public static Optional<UserDetails> getUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                       .map(authentication -> {
                           if (authentication.getPrincipal() instanceof UserDetails) {
                               return (UserDetails) authentication.getPrincipal();
                           }
                           return null;
                       });
    }
}
