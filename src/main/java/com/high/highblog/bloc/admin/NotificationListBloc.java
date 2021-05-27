package com.high.highblog.bloc.admin;

import com.high.highblog.enums.NotificationType;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Notification;
import com.high.highblog.service.notification.NotificationService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("adminNotificationListBloc")
public class NotificationListBloc {
    public final NotificationService notificationService;

    public NotificationListBloc(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @Transactional(readOnly = true)
    public Page<Notification> fetchAdminNotifications(BasePaginationReq req) {
        log.info("fetch list admin notifications");
        PageRequest pageRequest = PaginationHelper.generatePageRequest(req);
        NotificationType type = NotificationType.ADMIN;
        return  notificationService.fetchAdminNotifications(type,pageRequest);
    }
}
