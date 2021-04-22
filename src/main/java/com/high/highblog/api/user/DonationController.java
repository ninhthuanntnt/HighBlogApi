package com.high.highblog.api.user;

import com.high.highblog.bloc.payment.DonationBloc;
import com.high.highblog.model.dto.request.DonationReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/donations")
public class DonationController {

    private final DonationBloc donationBloc;

    public DonationController(final DonationBloc donationBloc) {
        this.donationBloc = donationBloc;
    }

    @PostMapping
    public ResponseEntity<?> donate(@RequestBody final DonationReq donationReq) {

        donationBloc.donate(donationReq);

        return ResponseEntity.noContent().build();
    }
}
