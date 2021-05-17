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
public class Currency {

    @SerializedName("currency_code")
    private String currencyCode;

    @SerializedName("value")
    private String value;
}
