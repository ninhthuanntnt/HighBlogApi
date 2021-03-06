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

    @Column(nullable = false)
    private String code;

    @Column
    private Long expiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "code_type", nullable = false)
    private CodeType codeType;

    public boolean isExpired() {
        long expirationDate = this.getExpiration() + this.getCreatedDate().getLong(ChronoField.MILLI_OF_SECOND);
        return expirationDate < Instant.now().getLong(ChronoField.MILLI_OF_SECOND);
    }
}