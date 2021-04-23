package com.high.highblog.api.user;

import com.high.highblog.bloc.UserTransactionBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.UserTransactionMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user/user-transactions")
public class UserTransactionController {

    private final UserTransactionBloc userTransactionBloc;

    public UserTransactionController(final UserTransactionBloc userTransactionBloc) {
        this.userTransactionBloc = userTransactionBloc;
    }
    @GetMapping
    public ResponseEntity<?> fetchTransactions(BasePaginationReq basePaginationReq) {
        Page<UserTransaction> userTransactions = userTransactionBloc.fetchUserTransactions(basePaginationReq);
        return ResponseEntity.ok(PaginationHelper
                .buildBasePaginationRes(userTransactions.map(UserTransactionMapper.INSTANCE::toUserTransactionRes)));
    }
}