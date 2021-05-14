package com.high.highblog.api.admin;

import com.high.highblog.bloc.UserTransactionBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.UserTransactionMapper;
import com.high.highblog.model.dto.request.admin.AdminTransactionReq;
import com.high.highblog.model.entity.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin/transactions")
public class AdminTransactionController {

    private final UserTransactionBloc userTransactionBloc;

    public AdminTransactionController(final UserTransactionBloc userTransactionBloc) {
        this.userTransactionBloc = userTransactionBloc;
    }
    @GetMapping
    public ResponseEntity<?> searchDynamicTransactions(AdminTransactionReq adminTransactionReq) {
        Page<UserTransaction> userTransactions = userTransactionBloc.searchDynamicTransactions(adminTransactionReq);
        return ResponseEntity.ok(PaginationHelper
                .buildBasePaginationRes(userTransactions.map(UserTransactionMapper.INSTANCE::toUserTransactionRes)));
    }
}