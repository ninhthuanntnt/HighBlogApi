package com.high.highblog.model.entity;

import com.high.highblog.enums.PaymentMethod;
import com.high.highblog.enums.PaymentType;
import com.high.highblog.enums.UserTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "hb_user_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserTransaction
        extends AbstractAuditingColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "transaction_no")
    private String transactionNo;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private UserTransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Transient
    private boolean isReceiver;

    private BigDecimal balance;
}
