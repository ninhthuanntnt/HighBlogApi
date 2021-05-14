package com.high.highblog.model.dto.request.admin;

import com.high.highblog.model.dto.request.BasePaginationReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminTransactionReq extends BasePaginationReq {
    private String nickName;
    private String transactionNo;
    private Long startDate;
    private Long endDate;
}
