package com.high.highblog.api.common;

import com.high.highblog.bloc.RegisterBloc;
import com.high.highblog.constant.Constant;
import com.high.highblog.model.dto.request.RegisterReq;
import com.high.highblog.model.dto.request.ResendEmailReq;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    public final RegisterBloc registerBloc;

    public RegisterController(final RegisterBloc registerBloc) {
        this.registerBloc = registerBloc;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterReq registerReq) {
        Long confirmationCodeId = registerBloc.register(registerReq);
        ResponseCookie cookie = ResponseCookie.from(Constant.REGISTRATION_CONFIRMATION_CODE_ID_COOKIE,
                                                    confirmationCodeId.toString())
                                              .secure(true)
                                              .httpOnly(true)
                                              .maxAge(Constant.Expiration.CONFIRMATION_CODE_EXPIRATION)
                                              .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                             .header(HttpHeaders.SET_COOKIE, cookie.toString())
                             .build();
    }

    @PostMapping("/activation/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable("id") Long confirmationCodeId,
                                             @RequestParam("code") String code) {
        registerBloc.activateAccount(confirmationCodeId, code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-email")
    public ResponseEntity<?> resendEmail(@CookieValue(name = Constant.REGISTRATION_CONFIRMATION_CODE_ID_COOKIE,
                                                      required = false) final Long confirmationCodeId,
                                         @RequestBody ResendEmailReq resendEmailReq) {

        registerBloc.resendConfirmRegistration(confirmationCodeId, resendEmailReq);
        return ResponseEntity.noContent().build();
    }
}
