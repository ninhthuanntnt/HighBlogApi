package com.high.highblog.bloc.admin;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.dto.request.admin.AdminTransactionReq;
import com.high.highblog.model.entity.UserTransaction;
import com.high.highblog.service.UserService;
import com.high.highblog.service.UserTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Slf4j
@Component("adminUserTransactionBloc")
public class UserTransactionBloc {
    private final UserTransactionService userTransactionService;
    private final UserService userService;

    public UserTransactionBloc(UserTransactionService userTransactionService, UserService userService) {
        this.userTransactionService = userTransactionService;
        this.userService = userService;
    }
    @Transactional(readOnly = true)
    public Page<UserTransaction> searchDynamicTransactions(AdminTransactionReq req) {
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        Long userId = null;
        Instant startDate = null ,endDate = null;
        String transactionNo = req.getTransactionNo();
        if(req.getNickName() != null)  userId = userService.getByNickName(req.getNickName()).getId();
        if(req.getStartDate() != null)  startDate = Instant.ofEpochMilli(req.getStartDate());
        if(req.getEndDate() != null)  endDate = Instant.ofEpochMilli(req.getEndDate());

        log.info("Fetch list transactions ");
        return userTransactionService.searchDynamicTransactions(userId,transactionNo,startDate,endDate,pageRequest);
    }
}
