package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.SystemTransactionBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.entity.SystemTransaction;
import liquibase.license.LicenseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/system-transaction")
public class SystemTransactionController {

    private final SystemTransactionBloc systemTransactionBloc;

    public SystemTransactionController(SystemTransactionBloc systemTransactionBloc) {
        this.systemTransactionBloc = systemTransactionBloc;
    }

    @GetMapping
    public ResponseEntity<?> fetchSystemTransactions(long transactionId) {
        List<SystemTransaction> systemTransactions = systemTransactionBloc.fetchSystemTransactions(transactionId);
        return ResponseEntity.ok(systemTransactions);
    }
}