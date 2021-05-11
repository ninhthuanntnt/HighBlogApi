package com.high.highblog.service.notification;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.model.entity.UserNotification;
import com.high.highblog.repository.UserNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserNotificationService {
    private final UserNotificationRepository repository;

    public UserNotificationService(final UserNotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveNewAll(final List<Long> userIds, final Long notificationId) {
        log.info("Save new all with userIds #{} and notificationId #{}", userIds, notificationId);

        List<UserNotification> userNotifications = userIds.stream()
                                                          .map(userId -> UserNotification.builder()
                                                                                         .userId(userId)
                                                                                         .notificationId(notificationId)
                                                                                         .build())
                                                          .collect(Collectors.toList());
        log.info("Data before save #{}", userNotifications);
        repository.save(userNotifications.get(0));
    }

    @Transactional
    public void markAsSentByIdAndUserId(final Long id, final Long userId){
        log.info("Mark as sent for notification by id #{}", id);

        UserNotification userNotification = repository.findByIdAndUserId(id, userId)
                                                      .orElseThrow(()->new ObjectNotFoundException("userNotification"));

        userNotification.setSent(true);

        repository.save(userNotification);
    }
}
