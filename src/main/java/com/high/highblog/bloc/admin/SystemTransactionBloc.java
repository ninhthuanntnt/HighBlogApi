package com.high.highblog.bloc.admin;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.admin.SystemTransactionReq;
import com.high.highblog.model.entity.SystemTransaction;
import com.high.highblog.service.SystemTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Component("adminSystemTransactionBloc")
public class SystemTransactionBloc {
    private final SystemTransactionService systemTransactionService;

    public SystemTransactionBloc(SystemTransactionService systemTransactionService) {
        this.systemTransactionService = systemTransactionService;
    }
    @Transactional(readOnly = true)
    public List<SystemTransaction> fetchSystemTransactions(Long senderId) {
        log.info("fetch system transaction and third party by senderId#{}",senderId);
        return systemTransactionService.fetchSystemTransactions(senderId);
    }
}
