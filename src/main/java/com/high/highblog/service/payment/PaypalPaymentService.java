package com.high.highblog.service.payment;

import com.high.highblog.enums.CurrencyType;
import com.high.highblog.enums.ThirdPartyTransactionStatus;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.entity.ThirdPartyTransaction;
import com.high.highblog.repository.ThirdPartyTransactionRepository;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Capture;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;

import static com.high.highblog.enums.ThirdPartyTransactionStatus.CREATED;
import static com.high.highblog.enums.ThirdPartyTransactionStatus.valueOf;

@Slf4j
@Service
public class PaypalPaymentService
        implements PaymentService {
    private final PayPalHttpClient payPalHttpClient;
    private final ThirdPartyTransactionRepository repository;

    public PaypalPaymentService(final PayPalHttpClient payPalHttpClient,
                                final ThirdPartyTransactionRepository repository) {
        this.payPalHttpClient = payPalHttpClient;
        this.repository = repository;
    }

    @Override
    @Transactional
    public ThirdPartyTransaction createPayment(final BigDecimal amount) {
        log.info("Create paypal order with amount #{}", amount);
        try {
            OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest();
            ordersCreateRequest.requestBody(buildOrderRequest(amount));

            HttpResponse<Order> orderRes = payPalHttpClient.execute(ordersCreateRequest);

            ThirdPartyTransaction thirdPartyTransaction = buildCreatedThirdPartyTransaction(orderRes, amount);

            repository.save(thirdPartyTransaction);

            return thirdPartyTransaction;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ValidatorException("Unsuccessfully created payment", "paypal");
        }
    }

    @Override
    @Transactional
    public ThirdPartyTransaction executePayment(final String paymentId) {
        try {
            OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(paymentId);

            HttpResponse<Order> orderRes = payPalHttpClient.execute(ordersCaptureRequest);

            ThirdPartyTransaction thirdPartyTransaction = buildExecutedThirdPartyTransaction(orderRes);

            repository.save(thirdPartyTransaction);

            return thirdPartyTransaction;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ValidatorException("Unsuccessfully executed payment", "paypal");
        }
    }

    private ThirdPartyTransaction buildCreatedThirdPartyTransaction(final HttpResponse<Order> orderRes,
                                                                    final BigDecimal amount) {
        Order order = orderRes.result();

        String redirectUrl = order.links().get(1).href();

        ThirdPartyTransactionStatus status = ThirdPartyTransactionStatus.valueOf(order.status());

        return ThirdPartyTransaction.builder()
                                    .paymentId(order.id())
                                    .currencyType(CurrencyType.USD)
                                    .status(status)
                                    .amount(amount)
                                    .additionalParams(Collections.singletonMap("redirect_url", redirectUrl))
                                    .build();
    }

    private ThirdPartyTransaction buildExecutedThirdPartyTransaction(final HttpResponse<Order> orderRes) {
        Order order = orderRes.result();
        Capture capture = order.purchaseUnits()
                               .get(0)
                               .payments()
                               .captures()
                               .get(0);

        BigDecimal amount = new BigDecimal(capture.amount().value());

        BigDecimal fee = new BigDecimal(capture.sellerReceivableBreakdown().paypalFee().value());

        CurrencyType currencyType = CurrencyType.valueOf(capture.amount().currencyCode());

        ThirdPartyTransactionStatus status = ThirdPartyTransactionStatus.valueOf(capture.status());
        return ThirdPartyTransaction.builder()
                                    .paymentId(order.id())
                                    .currencyType(currencyType)
                                    .status(status)
                                    .amount(amount)
                                    .fee(fee)
                                    .additionalParams(new HashMap<String, Object>() {{
                                        put("payer", order.payer());
                                    }})
                                    .build();
    }

    private OrderRequest buildOrderRequest(final BigDecimal amount) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.shippingPreference("NO_SHIPPING");
        orderRequest.applicationContext(applicationContext);

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest();
        AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown();
        amountWithBreakdown.currencyCode("USD");
        amountWithBreakdown.value(amount.toString());
        purchaseUnitRequest.amountWithBreakdown(amountWithBreakdown);
        orderRequest.purchaseUnits(Collections.singletonList(purchaseUnitRequest));

        return orderRequest;
    }
}
