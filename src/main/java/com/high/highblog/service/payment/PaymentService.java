package com.high.highblog.service.payment;

import com.high.highblog.model.entity.ThirdPartyTransaction;

import java.math.BigDecimal;

public interface PaymentService {
    ThirdPartyTransaction createPayment(BigDecimal amount);

    ThirdPartyTransaction executePayment(String paymentId);
}
