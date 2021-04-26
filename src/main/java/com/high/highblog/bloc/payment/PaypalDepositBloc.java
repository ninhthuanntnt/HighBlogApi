package com.high.highblog.bloc.payment;

import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.PaymentCreateReq;
import com.high.highblog.model.dto.request.PaymentExecuteReq;
import com.high.highblog.model.dto.response.PaymentCreateRes;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import com.high.highblog.service.WalletService;
import com.high.highblog.service.payment.PaymentService;
import com.high.highblog.service.payment.PaypalPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
public class PaypalDepositBloc {

    private final PaymentService paymentService;
    private final WalletService walletService;
    private final TransactionBloc transactionBloc;

    public PaypalDepositBloc(final PaypalPaymentService paymentService,
                             final WalletService walletService,
                             final TransactionBloc transactionBloc) {
        this.paymentService = paymentService;
        this.walletService = walletService;
        this.transactionBloc = transactionBloc;
    }

    @Transactional
    public PaymentCreateRes createDeposit(final PaymentCreateReq paymentCreateReq) {
        ThirdPartyTransaction thirdPartyTransaction = paymentService.createPayment(paymentCreateReq.getAmount());

        transactionBloc.saveUserAndSystemTransaction(thirdPartyTransaction);

        return new PaymentCreateRes(thirdPartyTransaction.getPaymentId());
    }

    @Transactional
    public void executeDeposit(final PaymentExecuteReq paymentExecuteReq) {
        ThirdPartyTransaction thirdPartyTransaction = paymentService.executePayment(paymentExecuteReq.getPaymentId());

        transactionBloc.saveUserAndSystemTransaction(thirdPartyTransaction);

        BigDecimal additionalBalance = thirdPartyTransaction.getAmount().subtract(thirdPartyTransaction.getFee());

        walletService.addBalance(SecurityHelper.getUserId(), additionalBalance);

    }

    @Transactional
    public void cancelDeposit(final PaymentExecuteReq paymentExecuteReq) {
        ThirdPartyTransaction thirdPartyTransaction = paymentService.cancelPayment(paymentExecuteReq.getPaymentId());

        transactionBloc.saveUserAndSystemTransaction(thirdPartyTransaction);
    }
}
