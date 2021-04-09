package com.high.highblog.service;

import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.entity.Subscription;
import com.high.highblog.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionService(final SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndNickName(final Long userId, final String nickName) {
        log.info("Exists by userId #{} and nickName #{}", userId, nickName);

        return repository.existsByUserIdAndNickName(userId, nickName);
    }

    @Transactional
    public void saveNew(final Subscription subscription) {
        log.info("Save new subscription with data #{}", subscription);

        validatePostVoteBeforeSaveNew(subscription);

        repository.save(subscription);
    }

    @Transactional
    public void saveNew(final Long userId, final Long followerId) {
        log.info("Save new subscription with userId #{} and followerId #{}", userId, followerId);

        saveNew(Subscription.builder()
                            .userId(userId)
                            .followerId(followerId)
                            .build());
    }

    private void validatePostVoteBeforeSaveNew(final Subscription subscription) {
        if (ObjectUtils.isNotEmpty(subscription.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (subscription.getUserId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid user id", "userId");
    }

}
