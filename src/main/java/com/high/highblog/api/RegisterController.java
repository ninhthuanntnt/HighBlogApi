package com.high.highblog.api;

import com.high.highblog.bloc.RegisterBloc;
import com.high.highblog.model.dto.request.RegisterReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    public final RegisterBloc registerBloc;

    public RegisterController(final RegisterBloc registerBloc) {
        this.registerBloc = registerBloc;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterReq registerReq) {
        registerBloc.register(registerReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/activation/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable("id") Long confirmationCodeId,
                                             @RequestParam("code") String code) {
        registerBloc.activateAccount(confirmationCodeId, code);
        return ResponseEntity.noContent().build();
    }
}
