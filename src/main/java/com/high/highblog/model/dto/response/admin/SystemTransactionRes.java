package com.high.highblog.model.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.high.highblog.enums.SystemTransactionStatus;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemTransactionRes {
    private Long id;

    private Long senderTransactionId;

    private Long receiverTransactionId;

    private Long thirdPartyTransactionId;

    private ThirdPartyTransaction thirdPartyTransaction;

    private BigDecimal amount;

    private SystemTransactionStatus status;

    @Builder.Default
    private Float feeRate = 0F;
}
