package com.high.highblog.model.entity;

import com.high.highblog.enums.SystemTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "hb_system_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SystemTransaction
        extends AbstractAuditingColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sender_transaction_id")
    private Long senderTransactionId;

    @NotNull
    @Column(name = "receiver_transaction_id")
    private Long receiverTransactionId;

    @Column(name = "third_party_transaction_id")
    private Long thirdPartyTransactionId;

    @NotNull
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private SystemTransactionStatus status;

    @NotNull
    @Builder.Default
    @Column(name = "fee_rate")
    private Float feeRate = 0F;
}
