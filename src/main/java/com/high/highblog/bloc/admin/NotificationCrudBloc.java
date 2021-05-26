package com.high.highblog.bloc.admin;

import com.high.highblog.bloc.notification.NotificationBloc;
import com.high.highblog.enums.NotificationType;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.model.dto.request.admin.NotificationCreateReq;
import com.high.highblog.model.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service("adminNotificationCrudBloc")
public class NotificationCrudBloc {
    private final NotificationBloc notificationBloc;

    public NotificationCrudBloc(final NotificationBloc notificationBloc) {
        this.notificationBloc = notificationBloc;
    }

    public void createNotification(final NotificationCreateReq notificationCreateReq) {

        Long adminId = SecurityHelper.getUserId();
        Notification notification = Notification.builder().type(NotificationType.ADMIN)
                                                .content(notificationCreateReq.getMessage())
                                                .senderId(adminId)
                                                .sourceId(null)
                                                .build();

        notificationBloc.pushNotificationTo(notificationCreateReq.getReceiverIds(), notification);
    }
}
