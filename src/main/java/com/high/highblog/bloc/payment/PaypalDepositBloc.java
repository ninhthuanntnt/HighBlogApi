package com.high.highblog.bloc.payment;

import com.high.highblog.enums.PaymentMethod;
import com.high.highblog.enums.PaymentType;
import com.high.highblog.enums.SystemTransactionStatus;
import com.high.highblog.enums.UserTransactionStatus;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.CodeHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.PaymentCreateReq;
import com.high.highblog.model.dto.request.PaymentExecuteReq;
import com.high.highblog.model.dto.response.PaymentCreateRes;
import com.high.highblog.model.entity.SystemTransaction;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.model.entity.Wallet;
import com.high.highblog.service.SystemTransactionService;
import com.high.highblog.service.UserTransactionService;
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
    private final UserTransactionService userTransactionService;
    private final SystemTransactionService systemTransactionService;
    private final WalletService walletService;

    public PaypalDepositBloc(final PaypalPaymentService paymentService,
                             final UserTransactionService userTransactionService,
                             final SystemTransactionService systemTransactionService,
                             final WalletService walletService) {
        this.paymentService = paymentService;
        this.userTransactionService = userTransactionService;
        this.systemTransactionService = systemTransactionService;
        this.walletService = walletService;
    }

    @Transactional
    public PaymentCreateRes createPayment(final PaymentCreateReq paymentCreateReq) {
        ThirdPartyTransaction thirdPartyTransaction = paymentService.createPayment(paymentCreateReq.getAmount());

        saveUserAndSystemTransaction(thirdPartyTransaction);

        return new PaymentCreateRes(thirdPartyTransaction.getPaymentId());
    }

    @Transactional
    public void executePayment(final PaymentExecuteReq paymentExecuteReq) {
        ThirdPartyTransaction thirdPartyTransaction = paymentService.executePayment(paymentExecuteReq.getPaymentId());

        saveUserAndSystemTransaction(thirdPartyTransaction);

        Wallet wallet = walletService.getToSaveByUserId(SecurityHelper.getUserId());
        BigDecimal newBalance = wallet.getBalance()
                                      .add(thirdPartyTransaction.getAmount())
                                      .subtract(thirdPartyTransaction.getFee());

        walletService.saveBalance(wallet.getUserId(), newBalance);

    }

    private void saveUserAndSystemTransaction(final ThirdPartyTransaction thirdPartyTransaction) {
        // TODO: Need an converter to convert third party transaction to system and user transaction
        switch (thirdPartyTransaction.getStatus()) {
            case CREATED: {
                UserTransaction userTransaction = buildUserTransaction(thirdPartyTransaction,
                                                                       UserTransactionStatus.CREATED);
                userTransactionService.saveNew(userTransaction);
                systemTransactionService.saveNew(buildSystemTransaction(thirdPartyTransaction,
                                                                        userTransaction,
                                                                        SystemTransactionStatus.CREATED));
                break;
            }
            case APPROVED:
                break;
            case COMPLETED: {
                UserTransaction userTransaction = userTransactionService.getByPaymentId(thirdPartyTransaction
                                                                                                .getPaymentId());
                userTransactionService.saveStatus(userTransaction, UserTransactionStatus.FINISHED);
                systemTransactionService.saveNew(buildSystemTransaction(thirdPartyTransaction,
                                                                        userTransaction,
                                                                        SystemTransactionStatus.FINISHED));
                break;
            }
            case PENDING:
                break;
            case FAILED:
                break;
            default:
                throw new ValidatorException("Invalid transaction status (SERVER ERROR)", "status");
        }
    }

    private UserTransaction buildUserTransaction(final ThirdPartyTransaction thirdPartyTransaction,
                                                 final UserTransactionStatus userTransactionStatus) {
        Long userId = SecurityHelper.getUserId();
        Wallet wallet = walletService.getToSaveByUserId(userId);

        return UserTransaction.builder()
                              .userId(userId)
                              .transactionNo("TR-" + CodeHelper.generateUUID())
                              .amount(thirdPartyTransaction.getAmount())
                              .status(userTransactionStatus)
                              .paymentMethod(PaymentMethod.PAYPAL)
                              .paymentType(PaymentType.DEPOSIT)
                              .balance(wallet.getBalance())
                              .build();

    }

    private SystemTransaction buildSystemTransaction(final ThirdPartyTransaction thirdPartyTransaction,
                                                     final UserTransaction userTransaction,
                                                     final SystemTransactionStatus status) {
        return SystemTransaction.builder()
                                .senderTransactionId(userTransaction.getId())
                                .receiverTransactionId(userTransaction.getId())
                                .thirdPartyTransactionId(thirdPartyTransaction.getId())
                                .amount(userTransaction.getAmount())
                                .status(status)
                                .build();
    }
}
