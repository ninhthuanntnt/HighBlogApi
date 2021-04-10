package com.high.highblog.service;

import com.high.highblog.error.exception.ObjectNotFoundException;
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

    @Transactional
    public void delete(final Long userId, final Long followerId) {
        log.info("Delete subscription with userId #{} and followerId #{}", userId, followerId);

        Subscription subscription = repository.findByUserIdAndFollowerId(userId, followerId)
                                              .orElseThrow(() -> new ObjectNotFoundException("subscription"));

        repository.delete(subscription);
    }

    private void validatePostVoteBeforeSaveNew(final Subscription subscription) {
        if (ObjectUtils.isNotEmpty(subscription.getId()))
            throw new ValidatorException("Invalid post id", "id");
        if (subscription.getFollowerId() != SecurityHelper.getUserId())
            throw new ValidatorException("Invalid follower id", "followerId");
        if (repository.existsByUserIdAndFollowerId(subscription.getUserId(), subscription.getFollowerId()))
            throw new ValidatorException("Already exists", "subscription");
    }

}
