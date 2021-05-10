package com.high.highblog.bloc.notification;

import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Notification;
import com.high.highblog.model.entity.User;
import com.high.highblog.service.UserService;
import com.high.highblog.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NotificationListBloc {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationListBloc(final NotificationService notificationService,
                                final UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    public Page<Notification> fetchNotificationsForCurrentUser(final BasePaginationReq basePaginationReq) {
        Long userId = SecurityHelper.getUserId();
        log.info("Fetch notification for current userId #{} with page info #{}", userId, basePaginationReq);

        PageRequest pageRequest = PaginationHelper.generatePageRequestWithDefaultSort(basePaginationReq,
                                                                                      "-id");

        Page<Notification> notifications = notificationService.fetchByReceiverIdWithPageRequest(userId, pageRequest);

        includeSenderIdToNotifications(notifications);

        return notifications;
    }


    private void includeSenderIdToNotifications(final Page<Notification> notifications) {
        Set<Long> senderIds = notifications.map(Notification::getSenderId).stream().collect(Collectors.toSet());

        Map<Long, List<User>> senderIdsMap = userService.fetchByIdIn(senderIds)
                                                        .stream()
                                                        .collect(Collectors.groupingBy(User::getId));

        notifications.forEach(notification -> notification.setSender(senderIdsMap.get(notification.getSenderId())
                                                                                 .get(0)));
    }
}
