package com.high.highblog.api.user;

import com.high.highblog.bloc.notification.NotificationBloc;
import com.high.highblog.bloc.notification.NotificationListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.NotificationMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Notification;
import javafx.scene.control.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userNotificationListController")
@RequestMapping("/api/v1/user/notifications")
public class NotificationListController {

    private final NotificationListBloc notificationListBloc;

    public NotificationListController(final NotificationListBloc notificationListBloc) {
        this.notificationListBloc = notificationListBloc;
    }

    @GetMapping
    public ResponseEntity<?> fetchNotifications(final BasePaginationReq basePaginationReq) {
        Page<Notification> notifications = notificationListBloc.fetchNotificationsForCurrentUser(basePaginationReq);
        return ResponseEntity.ok(
                PaginationHelper.buildBasePaginationRes(
                        notifications.map(NotificationMapper.INSTANCE::toNotificationRes)));
    }
}
