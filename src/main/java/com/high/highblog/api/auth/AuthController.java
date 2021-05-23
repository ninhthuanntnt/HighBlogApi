package com.high.highblog.api.auth;

import com.high.highblog.bloc.JwtBloc;
import com.high.highblog.model.dto.request.LoginReq;
import com.high.highblog.model.dto.request.RefreshTokenReq;
import com.high.highblog.model.dto.response.TokenRes;
import com.high.highblog.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtBloc jwtBloc;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtBloc jwtBloc,
                          final AuthenticationManager authenticationManager) {
        this.jwtBloc = jwtBloc;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@Valid @RequestBody final LoginReq loginReq) {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                                                                              loginReq.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        TokenRes tokenRes = jwtBloc.generateToken(userDetails);

        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRes> refreshToken(@Valid @RequestBody final RefreshTokenReq refreshTokenReq) {

        // TODO: Get refresh token from Redis to make sure that it is exists for current user
        if (!jwtBloc.isExpiredToken(refreshTokenReq)) {
            return ResponseEntity.ok(jwtBloc.generateToken(refreshTokenReq));
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
