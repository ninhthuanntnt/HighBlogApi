package com.high.highblog.external.paypal.dto.reporting;

import com.paypal.http.HttpRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportingBalanceRequest
        extends HttpRequest<ReportingBalanceResponse> {

    public ReportingBalanceRequest() {
        super("/v1/reporting/balances?currency_code=USD", "GET", ReportingBalanceResponse.class);
    }

    public ReportingBalanceRequest authorization(String authorization) {
        header("Authorization", String.valueOf(authorization));
        return this;
    }

    public ReportingBalanceRequest contentType(String contentType) {
        header("Content-Type", String.valueOf(contentType));
        return this;
    }

    public ReportingBalanceRequest payPalPartnerAttributionId(String payPalPartnerAttributionId) {
        header("PayPal-Partner-Attribution-Id", String.valueOf(payPalPartnerAttributionId));
        return this;
    }

    public ReportingBalanceRequest prefer(String prefer) {
        header("Prefer", String.valueOf(prefer));
        return this;
    }
}
