package com.high.highblog.bloc.notification;

import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.mapper.NotificationMapper;
import com.high.highblog.model.dto.response.NotificationRes;
import com.high.highblog.model.entity.Notification;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.AccountService;
import com.high.highblog.service.SubscriptionService;
import com.high.highblog.service.UserService;
import com.high.highblog.service.notification.NotificationService;
import com.high.highblog.service.notification.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationBloc {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final SubscriptionService subscriptionService;
    private final AccountService accountService;
    private final UserService userService;

    private static final String DEFAULT_NOTIFICATION_DESTINATION = "/exchange/amq.direct/notification";

    public NotificationBloc(final SimpMessagingTemplate simpMessagingTemplate,
                            final NotificationService notificationService,
                            final UserNotificationService userNotificationService,
                            final SubscriptionService subscriptionService,
                            final AccountService accountService,
                            final UserService userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationService = notificationService;
        this.userNotificationService = userNotificationService;
        this.subscriptionService = subscriptionService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Async
    @Transactional
    public void pushNotificationToFollowers(final Long senderId,
                                            final Notification notification) {
        log.info("Push notification to followers with senderId #{} and data #{}",
                 senderId,
                 notification);

        List<Long> receiverIds = subscriptionService.fetchFollowerIdsByUserId(senderId);

        pushNotificationTo(receiverIds, notification);

    }

    @Async
    @Transactional
    public void pushNotificationTo(final List<Long> receiverIds,
                                   final Notification notification) {
        log.info("Push notification to receivers #{} with data #{}",
                 receiverIds,
                 notification);
        notificationService.saveNew(notification);

        userNotificationService.saveNewAll(receiverIds, notification.getId());

        NotificationRes notificationRes = NotificationMapper.INSTANCE.toNotificationRes(notification);

        accountService.fetchByUserIdIn(receiverIds)
                      .forEach(account -> simpMessagingTemplate.convertAndSendToUser(account.getUsername(),
                                                                                     DEFAULT_NOTIFICATION_DESTINATION,
                                                                                     notificationRes));
    }

    @Async
    @Transactional
    public void pushNotificationTo(final Long receiverId,
                                   final Notification notification) {
        log.info("Push notification to receiver #{} with data #{}",
                 receiverId,
                 notification);
        pushNotificationTo(Collections.singletonList(receiverId), notification);
    }

    @Async
    @Transactional
    public void deleteNotificationToFollowers(final Long sourceId) {
        log.info("Delete notification to followers by sourceId");

        notificationService.softDeleteBySourceId(sourceId);
    }

    @Transactional(readOnly = true)
    public void pushUnreadNotificationToCurrentUser() {
        Long userId = SecurityHelper.getUserId();
        List<Notification> unreadNotifications = notificationService.fetchUnreadNotifications(userId);

        populateSenderIdForNotifications(unreadNotifications);

        String username = SecurityHelper.getAccountUsername()
                                        .orElseThrow(() -> new ObjectNotFoundException("username"));


        List<NotificationRes> listNotificationRes = NotificationMapper.INSTANCE
                .toListNotificationRes(unreadNotifications);

        listNotificationRes.forEach(notificationRes ->
                                            simpMessagingTemplate.convertAndSendToUser(username,
                                                                                       DEFAULT_NOTIFICATION_DESTINATION,
                                                                                       notificationRes));
    }

    @Transactional
    public void markSentNotificationForCurrentUser(final Long notificationId) {
        Long userId = SecurityHelper.getUserId();

        log.info("Mark as sent notification for current user #{}", userId);

        userNotificationService.markAsSentByNotificationIdAndUserId(notificationId, userId);
    }

    private void populateSenderIdForNotifications(final List<Notification> notifications) {
        Set<Long> senderIds = notifications.stream()
                                           .map(Notification::getSenderId)
                                           .collect(Collectors.toSet());

        Map<Long, List<User>> senderIdsMap = userService.fetchByIdIn(senderIds)
                                                        .stream()
                                                        .collect(Collectors.groupingBy(User::getId));

        notifications.forEach(notification -> notification.setSender(senderIdsMap.get(notification.getSenderId())
                                                                                 .get(0)));
    }
}
