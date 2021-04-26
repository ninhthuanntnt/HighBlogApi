package com.high.highblog.bloc.payment;

import com.high.highblog.enums.PaymentMethod;
import com.high.highblog.enums.PaymentType;
import com.high.highblog.enums.SystemTransactionStatus;
import com.high.highblog.enums.UserTransactionStatus;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.CodeHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.SystemTransaction;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.model.entity.Wallet;
import com.high.highblog.service.SystemTransactionService;
import com.high.highblog.service.UserTransactionService;
import com.high.highblog.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionBloc {

    private final UserTransactionService userTransactionService;
    private final SystemTransactionService systemTransactionService;
    private final WalletService walletService;

    public TransactionBloc(final UserTransactionService userTransactionService,
                           final SystemTransactionService systemTransactionService,
                           final WalletService walletService) {
        this.userTransactionService = userTransactionService;
        this.systemTransactionService = systemTransactionService;
        this.walletService = walletService;
    }

    public void saveUserAndSystemTransaction(final ThirdPartyTransaction thirdPartyTransaction) {
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
            case COMPLETED:
            case SUCCESS: {
                UserTransaction userTransaction = userTransactionService.getNullableByPaymentId(thirdPartyTransaction
                                                                                                        .getPaymentId());

                if (ObjectUtils.isEmpty(userTransaction)) {
                    userTransaction = buildUserTransaction(thirdPartyTransaction,
                                                           UserTransactionStatus.FINISHED);
                    userTransactionService.saveNew(userTransaction);
                } else {
                    userTransactionService.saveStatus(userTransaction, UserTransactionStatus.FINISHED);
                }

                systemTransactionService.saveNew(buildSystemTransaction(thirdPartyTransaction,
                                                                        userTransaction,
                                                                        SystemTransactionStatus.FINISHED));
                break;
            }
            case PENDING:{
                UserTransaction userTransaction = userTransactionService.getNullableByPaymentId(thirdPartyTransaction
                                                                                                        .getPaymentId());
                if (ObjectUtils.isEmpty(userTransaction)) {
                    userTransaction = buildUserTransaction(thirdPartyTransaction,
                                                           UserTransactionStatus.IN_PROGRESS);
                    userTransactionService.saveNew(userTransaction);
                } else {
                    userTransactionService.saveStatus(userTransaction, UserTransactionStatus.IN_PROGRESS);
                }

                systemTransactionService.saveNew(buildSystemTransaction(thirdPartyTransaction,
                                                                        userTransaction,
                                                                        SystemTransactionStatus.IN_PROGRESS));

                break;
            }
            case FAILED:
                 break;
            case CANCELED: {
                UserTransaction userTransaction = userTransactionService.getNullableByPaymentId(thirdPartyTransaction
                                                                                                        .getPaymentId());

                userTransactionService.saveStatus(userTransaction, UserTransactionStatus.CANCELED);
                systemTransactionService.saveNew(buildSystemTransaction(thirdPartyTransaction,
                                                                        userTransaction,
                                                                        SystemTransactionStatus.CANCELED));
                break;
            }
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
