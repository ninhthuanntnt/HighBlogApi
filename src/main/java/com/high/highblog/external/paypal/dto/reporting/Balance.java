package com.high.highblog.external.paypal.dto.reporting;

import com.paypal.http.annotations.Model;
import com.paypal.http.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Model
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Balance {

    @SerializedName("currency")
    private String currency;

    @SerializedName("primary")
    private boolean primary;

    @SerializedName("total_balance")
    private Currency totalBalance;

    @SerializedName("available_balance")
    private Currency availableBalance;

    @SerializedName("withheld_balance")
    private Currency withheldBalance;
}
