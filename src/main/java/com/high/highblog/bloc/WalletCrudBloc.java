package com.high.highblog.bloc;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.response.WalletRes;
import com.high.highblog.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
public class WalletCrudBloc {
    private final WalletService walletService;

    public WalletCrudBloc(final WalletService walletService) {
        this.walletService = walletService;
    }
    @Transactional(readOnly = true)
    public WalletRes getBalance(){
        Long userId = SecurityHelper.getUserId();
        log.info("get balance of userId=#{}",userId);
        BigDecimal balance = walletService.getBalance(userId).getBalance();
        return new WalletRes(balance);
    }
}
