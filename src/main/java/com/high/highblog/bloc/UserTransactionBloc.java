package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.service.SystemTransactionService;
import com.high.highblog.service.UserTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserTransactionBloc {
    private final UserTransactionService userTransactionService;
    private final SystemTransactionService systemTransactionService;

    public UserTransactionBloc(UserTransactionService userTransactionService, SystemTransactionService systemTransactionService) {
        this.userTransactionService = userTransactionService;
        this.systemTransactionService = systemTransactionService;
    }

    @Transactional(readOnly = true)
    public Page<UserTransaction> fetchUserTransactions(BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        Long currentUserId = SecurityHelper.getUserId();

        log.info("Fetch transactions of user userId#{}", currentUserId);
        Page<UserTransaction> page = userTransactionService.fetchAllByUserId(currentUserId, pageRequest);
        page.forEach(userTransaction -> userTransaction.setReceiver(
                systemTransactionService.existByReceiverId(userTransaction.getId())
        ));
        return page;
    }
}
