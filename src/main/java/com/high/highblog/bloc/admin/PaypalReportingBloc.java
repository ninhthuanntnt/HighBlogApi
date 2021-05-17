package com.high.highblog.bloc.admin;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.external.paypal.dto.reporting.Balance;
import com.high.highblog.external.paypal.dto.reporting.ReportingBalanceRequest;
import com.high.highblog.external.paypal.dto.reporting.ReportingBalanceResponse;
import com.high.highblog.model.dto.response.admin.AdminBalanceRes;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
public class PaypalReportingBloc {
    private final PayPalHttpClient payPalHttpClient;

    public PaypalReportingBloc(final PayPalHttpClient payPalHttpClient) {
        this.payPalHttpClient = payPalHttpClient;
    }

    public AdminBalanceRes getBalance() {
        log.info("Get admin paypal balance");
        try {
            HttpResponse<ReportingBalanceResponse> balanceHttpRes = payPalHttpClient.execute(new ReportingBalanceRequest());

            if (balanceHttpRes.statusCode() == HttpStatus.OK.value()) {
                ReportingBalanceResponse balanceRes = balanceHttpRes.result();
                Balance balance = balanceRes.getBalances().get(0);

                return new AdminBalanceRes(new BigDecimal(balance.getTotalBalance().getValue()),
                                           new BigDecimal(balance.getAvailableBalance().getValue()),
                                           new BigDecimal(balance.getWithheldBalance().getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ValidatorException("Unsuccessful when get balance", "balance");
    }
}
