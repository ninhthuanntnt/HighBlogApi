package com.high.highblog.api.user;

import com.high.highblog.bloc.payment.PaypalWithdrawalBloc;
import com.high.highblog.model.dto.request.PaypalWithdrawReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/withdrawal/paypal")
public class PaypalWithdrawalController {
    private final PaypalWithdrawalBloc paypalWithdrawalBloc;

    public PaypalWithdrawalController(final PaypalWithdrawalBloc paypalWithdrawalBloc) {
        this.paypalWithdrawalBloc = paypalWithdrawalBloc;
    }

    @PostMapping
    public ResponseEntity<?> withdraw(@RequestBody final PaypalWithdrawReq paypalWithdrawReq) {

        paypalWithdrawalBloc.withdraw(paypalWithdrawReq);

        return ResponseEntity.noContent().build();
    }
}
