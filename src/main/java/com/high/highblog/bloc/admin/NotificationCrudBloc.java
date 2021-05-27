package com.high.highblog.bloc.admin;

import com.high.highblog.bloc.notification.NotificationBloc;
import com.high.highblog.enums.NotificationType;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.admin.NotificationCreateReq;
import com.high.highblog.model.entity.Notification;
import com.high.highblog.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("adminNotificationCrudBloc")
public class NotificationCrudBloc {
    private final NotificationBloc notificationBloc;
    public final NotificationService notificationService;

    public NotificationCrudBloc(final NotificationBloc notificationBloc, NotificationService notificationService) {
        this.notificationBloc = notificationBloc;
        this.notificationService = notificationService;
    }

    public void createNotification(final NotificationCreateReq notificationCreateReq) {

        Long adminId = SecurityHelper.getUserId();
        Notification notification = Notification.builder().type(NotificationType.ADMIN)
                                                .content(notificationCreateReq.getMessage())
                                                .senderId(adminId)
                                                .sourceId(0L)
                                                .build();

        notificationBloc.pushNotificationTo(notificationCreateReq.getReceiverIds(), notification);
    }
}
