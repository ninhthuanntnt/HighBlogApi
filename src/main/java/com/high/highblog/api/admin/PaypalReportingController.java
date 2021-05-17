package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.PaypalReportingBloc;
import com.high.highblog.model.dto.response.admin.AdminBalanceRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/paypal/reporting")
public class PaypalReportingController {

    private final PaypalReportingBloc paypalReportingBloc;

    public PaypalReportingController(final PaypalReportingBloc paypalReportingBloc) {
        this.paypalReportingBloc = paypalReportingBloc;
    }

    @GetMapping("/balances")
    public AdminBalanceRes getBalances() {

        return paypalReportingBloc.getBalance();
    }
}
