package com.high.highblog.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.high.highblog.enums.PaymentMethod;
import com.high.highblog.enums.PaymentType;
import com.high.highblog.enums.UserTransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTransactionRes {

    private Long id;

    private Long userId;

    private String transactionNo;

    private BigDecimal amount;

    private UserTransactionStatus status;

    private PaymentMethod paymentMethod;

    private PaymentType paymentType;

    private boolean isReceiver;

    private BigDecimal balance;

    private Instant createdDate;

}
