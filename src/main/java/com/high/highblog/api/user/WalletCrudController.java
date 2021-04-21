package com.high.highblog.api.user;

import com.high.highblog.bloc.WalletCrudBloc;
import com.high.highblog.model.dto.response.WalletRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/wallets ")
public class WalletCrudController {

    private final WalletCrudBloc walletCrudBloc;

    public WalletCrudController(final WalletCrudBloc walletCrudBloc) {
        this.walletCrudBloc = walletCrudBloc;
    }
    @GetMapping
    public ResponseEntity<?> getBalance(){
        WalletRes balance = walletCrudBloc.getBalance();
        return ResponseEntity.ok(balance);
    }
}