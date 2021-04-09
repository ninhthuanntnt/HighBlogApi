package com.high.highblog.api.user;

import com.high.highblog.bloc.SubscriptionCrudBloc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/subscriptions")
public class SubscriptionCrudController {
    private final SubscriptionCrudBloc subscriptionCrudBloc;

    public SubscriptionCrudController(final SubscriptionCrudBloc subscriptionCrudBloc) {
        this.subscriptionCrudBloc = subscriptionCrudBloc;
    }

    @PostMapping("/users/{nickName}")
    public ResponseEntity<?> follow(@PathVariable String nickName) {

        subscriptionCrudBloc.createSubscriptionForCurrentUser(nickName);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
