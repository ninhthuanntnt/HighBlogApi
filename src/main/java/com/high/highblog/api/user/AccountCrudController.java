package com.high.highblog.api.user;

import com.high.highblog.bloc.AccountCrudBloc;
import com.high.highblog.model.dto.request.PasswordUpdateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class AccountCrudController {

    private final AccountCrudBloc accountCrudBloc;

    public AccountCrudController(final AccountCrudBloc accountCrudBloc) {
        this.accountCrudBloc = accountCrudBloc;
    }

    @PutMapping("/users/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordUpdateReq passwordUpdateReq) {
        accountCrudBloc.changePasswordForCurrentAccount(passwordUpdateReq);
        return ResponseEntity.noContent().build();
    }
}
