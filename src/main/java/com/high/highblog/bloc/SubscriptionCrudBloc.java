package com.high.highblog.bloc;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.SubscriptionService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SubscriptionCrudBloc {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionCrudBloc(final SubscriptionService subscriptionService,
                                final UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @Transactional
    public void createSubscriptionForCurrentUser(final String nickName) {
        log.info("Create subscription for current user");

        Long userId = SecurityHelper.getUserId();

        validateUserIdAndNickName(userId, nickName);

        User follower = userService.getByNickName(nickName);

        subscriptionService.saveNew(userId, follower.getId());
    }

    private void validateUserIdAndNickName(final Long userId, final String nickName) {

        if(subscriptionService.existsByUserIdAndNickName(userId, nickName))
            throw new ValidatorException("Already exists subscription", "subscription");
    }
}
