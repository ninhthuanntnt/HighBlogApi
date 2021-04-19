package com.high.highblog.api.user;

import com.high.highblog.bloc.payment.PaypalDepositBloc;
import com.high.highblog.model.dto.request.PaymentCreateReq;
import com.high.highblog.model.dto.request.PaymentExecuteReq;
import com.high.highblog.model.dto.response.PaymentCreateRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/deposit/paypal")
public class PaypalDepositController {

    private final PaypalDepositBloc paypalDepositBloc;

    public PaypalDepositController(final PaypalDepositBloc paypalDepositBloc) {
        this.paypalDepositBloc = paypalDepositBloc;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentCreateRes> createPayment(@RequestBody final PaymentCreateReq paymentCreateReq) {

        return ResponseEntity.ok(paypalDepositBloc.createPayment(paymentCreateReq));
    }

    @PostMapping("/execute")
    public ResponseEntity<?> createPayment(@RequestBody final PaymentExecuteReq paymentExecuteReq) {
        paypalDepositBloc.executePayment(paymentExecuteReq);
        return ResponseEntity.ok().build();
    }
}
