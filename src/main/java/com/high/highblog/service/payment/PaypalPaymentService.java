package com.high.highblog.service.payment;

import com.high.highblog.enums.CurrencyType;
import com.high.highblog.enums.ThirdPartyTransactionStatus;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.CodeHelper;
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
import com.paypal.payouts.CreatePayoutRequest;
import com.paypal.payouts.CreatePayoutResponse;
import com.paypal.payouts.Currency;
import com.paypal.payouts.PayoutBatch;
import com.paypal.payouts.PayoutHeader;
import com.paypal.payouts.PayoutItem;
import com.paypal.payouts.PayoutsGetRequest;
import com.paypal.payouts.PayoutsPostRequest;
import com.paypal.payouts.SenderBatchHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public ThirdPartyTransaction withdraw(final String email, final BigDecimal amount) {
        log.info("Withdraw to paypal account #{} with amount #{}", email, amount);
        try {
            PayoutsPostRequest request = buildPayoutsPostRequest(email, amount);
            HttpResponse<CreatePayoutResponse> response = payPalHttpClient.execute(request);

            CreatePayoutResponse createPayoutResponse = response.result();

            PayoutHeader payoutHeader = createPayoutResponse.batchHeader();

            PayoutsGetRequest payoutsGetRequest = new PayoutsGetRequest(payoutHeader.payoutBatchId());

            HttpResponse<PayoutBatch> payoutBatchRes = payPalHttpClient.execute(payoutsGetRequest);
            PayoutBatch payoutBatch = payoutBatchRes.result();

            HashMap<String, Object> additionalParams = new HashMap<String, Object>(){{
                put("senderBatchId", payoutHeader.senderBatchHeader().senderBatchId());
                put("senderItemId", payoutBatch.items().get(0).payoutItem().senderItemId());
                put("receiverEmail", email);
            }};
            ThirdPartyTransaction thirdPartyTransaction =
                    ThirdPartyTransaction.builder()
                                         .paymentId(payoutHeader.payoutBatchId())
                                         .amount(amount)
                                         .status(ThirdPartyTransactionStatus.valueOf(payoutBatch.items()
                                                                                                .get(0)
                                                                                                .transactionStatus()))
                                         .fee(new BigDecimal(payoutBatch.batchHeader()
                                                                        .fees()
                                                                        .value()))
                                         .additionalParams(additionalParams)
                                         .build();

            repository.save(thirdPartyTransaction);

            return thirdPartyTransaction;


        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();

            throw new ValidatorException("Unsuccessfully withdraw", "paypal");
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

    private PayoutsPostRequest buildPayoutsPostRequest(final String email, final BigDecimal amount) {
        CreatePayoutRequest createPayoutRequest = new CreatePayoutRequest();

        SenderBatchHeader senderBatchHeader = new SenderBatchHeader();
        senderBatchHeader.recipientType("EMAIL");
        senderBatchHeader.senderBatchId("BATCH_ID_HB-" + CodeHelper.generateUUID());
        senderBatchHeader.emailSubject("Withdraw from your high blog account");
        senderBatchHeader.emailMessage("Thank you! for using our service");
        createPayoutRequest.senderBatchHeader(senderBatchHeader);

        List<PayoutItem> payoutItems = new ArrayList<>();
        PayoutItem payoutItem = new PayoutItem();
        payoutItem.recipientType("EMAIL");
        payoutItem.amount(new Currency().value(amount.toString()).currency(CurrencyType.USD.name()));
        payoutItem.senderItemId(CodeHelper.generateUUID());
        payoutItem.note("Thank for your patronage!");
        payoutItem.receiver(email);

        payoutItems.add(payoutItem);

        createPayoutRequest.items(payoutItems);

        return new PayoutsPostRequest().requestBody(createPayoutRequest);
    }
}
