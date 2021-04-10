package com.high.highblog.bloc;

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

        Long followerId = SecurityHelper.getUserId();

        User user = userService.getByNickName(nickName);

        subscriptionService.saveNew(user.getId(), followerId);
    }

    @Transactional
    public void deleteSubscriptionForCurrentUser(final String nickName) {
        log.info("Delete subscription for current user");

        Long followerId = SecurityHelper.getUserId();

        User user = userService.getByNickName(nickName);

        subscriptionService.delete(user.getId(), followerId);
    }
}
