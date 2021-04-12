package com.high.highblog.model.entity;

import com.high.highblog.enums.CodeType;
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

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "hb_confirmation_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ConfirmationCode
        extends AbstractAuditingColumns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    private String code;

    @Column
    private Long expiration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "code_type")
    private CodeType codeType;

    @NotNull
    @Builder.Default
    private boolean activated = true;

    public boolean isExpired() {
        long expirationDate = this.getExpiration() + this.getCreatedDate().toEpochMilli();
        return expirationDate < Instant.now().toEpochMilli();
    }
}