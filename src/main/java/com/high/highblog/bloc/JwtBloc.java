package com.high.highblog.bloc;

import com.high.highblog.config.ApplicationConfigProperties;
import com.high.highblog.constant.AppErrorCode;
import com.high.highblog.error.exception.InvalidJwtToken;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.dto.request.RefreshTokenReq;
import com.high.highblog.model.dto.response.TokenRes;
import com.high.highblog.model.entity.Account;
import com.high.highblog.model.entity.Role;
import com.high.highblog.security.CustomUserDetails;
import com.high.highblog.service.AccountService;
import com.high.highblog.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtBloc {

    private final ApplicationConfigProperties.Jwt jwtConfigProperties;
    private final AccountService accountService;
    private final RoleService roleService;

    public JwtBloc(final ApplicationConfigProperties applicationConfigProperties,
                   final AccountService accountService,
                   final RoleService roleService) {
        this.jwtConfigProperties = applicationConfigProperties.getJwt();
        this.accountService = accountService;
        this.roleService = roleService;
    }

    public TokenRes generateToken(CustomUserDetails userDetails) {
        log.info("Generate token from UserDetails data #{}", userDetails);

        List<String> listAuthorities = userDetails.getAuthorities().stream()
                                                  .map(GrantedAuthority::getAuthority)
                                                  .collect(Collectors.toList());

        JwtBuilder jwtBuilder = generateStandardJwtBuilder(userDetails);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", listAuthorities);
        claims.put("user_id", userDetails.getUserId());
        claims.put("username", userDetails.getUsername());

        Date expiration = new Date(System.currentTimeMillis()
                                           + jwtConfigProperties.getExpiration());
        Date refreshExpiration = new Date(System.currentTimeMillis()
                                                  + jwtConfigProperties.getRefreshExpiration());
        String accessToken = jwtBuilder.addClaims(claims)
                                       .setExpiration(expiration)
                                       .signWith(SignatureAlgorithm.HS512,
                                                 jwtConfigProperties.getSecretKey())
                                       .compact();

        // TODO: Generate refresh token then store it into Redis
        String refreshToken = jwtBuilder.setExpiration(refreshExpiration)
                                        .signWith(SignatureAlgorithm.HS512, jwtConfigProperties
                                                .getSecretKey())
                                        .compact();

        return new TokenRes(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public TokenRes generateToken(RefreshTokenReq refreshTokenReq) {
        log.info("Generate token from refresh token #{}", refreshTokenReq);

        CustomUserDetails customUserDetails =
                getUserDetailsFromToken(refreshTokenReq.getRefreshToken())
                        .map(userDetails -> {
                            Account account = accountService.getAccountById(userDetails.getId());
                            List<Role> roles = roleService.fetchRolesByAccountId(account.getId());

                            return CustomUserDetails.builder()
                                                    .id(account.getId())
                                                    .userId(account.getUserId())
                                                    .username(account.getUsername())
                                                    .authorities(roles.stream()
                                                                      .map(role -> new SimpleGrantedAuthority(role.getRoleType()
                                                                                                                  .name()))
                                                                      .collect(Collectors.toList()))
                                                    .build();
                        }).orElseThrow(() -> new ObjectNotFoundException("userDetail"));

        return this.generateToken(customUserDetails);
    }

    private JwtBuilder generateStandardJwtBuilder(final CustomUserDetails userDetails) {

        String subject = String.valueOf(userDetails.getId()) + System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());

        return Jwts.builder()
                   .addClaims(claims)
                   .setSubject(subject)
                   .setIssuer(jwtConfigProperties.getIssuer())
                   .setIssuedAt(new Date());
    }

    @SuppressWarnings("unchecked")
    public Optional<CustomUserDetails> getUserDetailsFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(jwtConfigProperties.getSecretKey())
                                .parseClaimsJws(token)
                                .getBody();

            Long accountId = claims.get("id", Long.class);
            Long userId = claims.get("user_id", Long.class);
            String username = claims.get("username", String.class);

            List<String> listAuthorities = claims.get("authorities", List.class);

            List<GrantedAuthority> listGrantedAuthority = listAuthorities.stream()
                                                                         .map(SimpleGrantedAuthority::new)
                                                                         .collect(Collectors.toList());
            return Optional.of(new CustomUserDetails(accountId,
                                                     userId,
                                                     username,
                                                     null,
                                                     listGrantedAuthority));

        } catch (Exception e) {
            log.error("Cannot get user info from token -> Message: #{}", e.getMessage());
        }
        return Optional.empty();
    }

    public boolean isExpiredToken(final RefreshTokenReq refreshTokenReq) {
        try {
            Jwts.parser().setSigningKey(jwtConfigProperties.getSecretKey())
                .parseClaimsJws(refreshTokenReq.getRefreshToken());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (JwtException ex) {
            log.info("Token is not an expired token");
        }
        return false;
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfigProperties.getSecretKey()).parseClaimsJws(token);
        } catch (MalformedJwtException | SignatureException ex) {
            log.info("Invalid JWT token");
            throw new InvalidJwtToken(AppErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token");
            throw new InvalidJwtToken(AppErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT token");
            throw new InvalidJwtToken(AppErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims string is empty");
            throw new InvalidJwtToken(AppErrorCode.EMPTY_CLAIMS);
        }
    }
}
