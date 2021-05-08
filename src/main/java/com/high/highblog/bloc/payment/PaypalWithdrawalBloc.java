package com.high.highblog.bloc.payment;

import com.high.highblog.enums.PaymentType;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.PaypalWithdrawReq;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import com.high.highblog.model.entity.Wallet;
import com.high.highblog.service.WalletService;
import com.high.highblog.service.payment.PaymentService;
import com.high.highblog.service.payment.PaypalPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
public class PaypalWithdrawalBloc {

    private final PaymentService paymentService;
    private final WalletService walletService;
    private final TransactionBloc transactionBloc;

    public PaypalWithdrawalBloc(final PaypalPaymentService paymentService,
                                final WalletService walletService,
                                final TransactionBloc transactionBloc) {
        this.paymentService = paymentService;
        this.walletService = walletService;
        this.transactionBloc = transactionBloc;
    }

    @Transactional
    public void withdraw(final PaypalWithdrawReq paypalWithdrawReq) {

        walletService.subtractBalanceIfSufficient(SecurityHelper.getUserId(),
                                                  paypalWithdrawReq.getAmount());

        ThirdPartyTransaction thirdPartyTransaction = paymentService.withdraw(paypalWithdrawReq.getEmail(),
                                                                              paypalWithdrawReq.getAmount());

        transactionBloc.saveUserAndSystemTransaction(thirdPartyTransaction, PaymentType.WITHDRAW);
    }
}
