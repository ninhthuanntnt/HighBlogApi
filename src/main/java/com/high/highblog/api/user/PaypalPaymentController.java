package com.high.highblog.api.user;

import com.high.highblog.bloc.payment.PaypalPaymentBloc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/paypal")
public class PaypalPaymentController {

    private final PaypalPaymentBloc paypalPaymentBloc;

    public PaypalPaymentController(final PaypalPaymentBloc paypalPaymentBloc) {
        this.paypalPaymentBloc = paypalPaymentBloc;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment() {

        return ResponseEntity.ok().build();
    }
}
