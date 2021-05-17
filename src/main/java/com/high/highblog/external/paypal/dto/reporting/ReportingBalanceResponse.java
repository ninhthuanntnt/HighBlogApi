package com.high.highblog.external.paypal.dto.reporting;

import com.paypal.http.annotations.Model;
import com.paypal.http.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Model
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportingBalanceResponse {

    @SerializedName(value = "balances", listClass = Balance.class)
    private List<Balance> balances;

    @SerializedName("as_of_time")
    private String asOfTime;

    @SerializedName("account_id")
    private String accountId;

    @SerializedName("last_refresh_time")
    private String lastRefreshTime;
}
