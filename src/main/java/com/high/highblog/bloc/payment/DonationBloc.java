package com.high.highblog.bloc.payment;

import com.high.highblog.enums.PaymentMethod;
import com.high.highblog.enums.PaymentType;
import com.high.highblog.enums.SystemTransactionStatus;
import com.high.highblog.enums.UserTransactionStatus;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.CodeHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.DonationReq;
import com.high.highblog.model.entity.SystemTransaction;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.model.entity.Wallet;
import com.high.highblog.service.SystemTransactionService;
import com.high.highblog.service.UserService;
import com.high.highblog.service.UserTransactionService;
import com.high.highblog.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PessimisticLockException;

import java.math.BigDecimal;

@Slf4j
@Component
public class DonationBloc {

    private final UserTransactionService userTransactionService;
    private final SystemTransactionService systemTransactionService;
    private final WalletService walletService;
    private final UserService userService;

    public DonationBloc(final UserTransactionService userTransactionService,
                        final SystemTransactionService systemTransactionService,
                        final WalletService walletService,
                        final UserService userService) {
        this.userTransactionService = userTransactionService;
        this.systemTransactionService = systemTransactionService;
        this.walletService = walletService;
        this.userService = userService;
    }

    @Transactional
    public void donate(final DonationReq donationReq) {
        log.info("Donate for user with nickName #{} with amount #{}",
                 donationReq.getNickName(),
                 donationReq.getAmount());

        // TODO: Use queue to handle transactions
        // TODO: Handle pessimistic locking exception then create IN_PROGRESS transaction and response immediately

        BigDecimal amount = donationReq.getAmount();

        Long senderId = SecurityHelper.getUserId();
        Long receiverId = userService.getByNickName(donationReq.getNickName()).getId();

        boolean locked = true;
        while (locked) {
            try {
                Wallet senderWallet = walletService.getToSaveByUserId(senderId);
                Wallet receiverWallet = walletService.getToSaveByUserId(receiverId);
                locked = false;

                validateSenderWalletAndAmount(senderWallet, amount);

                UserTransaction senderTransaction = buildUserTransaction(senderId,
                                                                         amount,
                                                                         senderWallet.getBalance());

                UserTransaction receiverTransaction = buildUserTransaction(receiverId,
                                                                           amount,
                                                                           receiverWallet.getBalance());

                userTransactionService.saveNew(senderTransaction);
                userTransactionService.saveNew(receiverTransaction);

                SystemTransaction systemTransaction = buildSystemTransaction(senderTransaction.getId(),
                                                                             receiverTransaction.getId(),
                                                                             amount);

                systemTransactionService.saveNew(systemTransaction);

                walletService.subtractBalanceIfSufficient(senderId, amount);
                walletService.addBalance(receiverId, amount);

            } catch (PessimisticLockException ex) {
                log.info("Pessimistic lock when transfer money from userId #{} to #{}", senderId, receiverId);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
                locked = true;
            }
        }
    }

    private void validateSenderWalletAndAmount(final Wallet senderWallet, final BigDecimal amount) {

        if(senderWallet.getBalance().compareTo(amount) < 0){
            throw new ValidatorException("Insufficient", "wallet");
        }
    }

    private UserTransaction buildUserTransaction(final Long userId,
                                                 final BigDecimal amount,
                                                 final BigDecimal balance) {

        return UserTransaction.builder()
                              .userId(userId)
                              .transactionNo("TR-" + CodeHelper.generateUUID())
                              .amount(amount)
                              .status(UserTransactionStatus.FINISHED)
                              .paymentMethod(PaymentMethod.WALLET)
                              .paymentType(PaymentType.DONATE)
                              .balance(balance)
                              .build();

    }

    private SystemTransaction buildSystemTransaction(final Long senderTransactionId,
                                                     final Long receiverTransactionId,
                                                     final BigDecimal amount) {

        return SystemTransaction.builder()
                                .senderTransactionId(senderTransactionId)
                                .receiverTransactionId(receiverTransactionId)
                                .thirdPartyTransactionId(null)
                                .amount(amount)
                                .status(SystemTransactionStatus.FINISHED)
                                .build();
    }
}
