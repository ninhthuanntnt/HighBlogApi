package com.high.highblog.bloc;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.UserTransaction;
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

    public UserTransactionBloc(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

    @Transactional(readOnly = true)
    public Page<UserTransaction> fetchUserTransactions(BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        Long currentUserId = SecurityHelper.getUserId();
        log.info("Fetch transactions of user userId#{}", currentUserId);
        return userTransactionService.fetchAllByUserId(currentUserId, pageRequest);
    }

    public Page<UserTransaction> fetchListTransactions(BasePaginationReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        log.info("Fetch list transactions ");
        return userTransactionService.fetchAll(pageRequest);
    }
}
