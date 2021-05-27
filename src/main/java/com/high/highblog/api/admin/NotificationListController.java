package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.NotificationListBloc;
import com.high.highblog.helper.PaginationHelper;
import com.high.highblog.mapper.NotificationMapper;
import com.high.highblog.model.dto.request.BasePaginationReq;
import com.high.highblog.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("adminNotificationListController")
@RequestMapping("/api/v1/admin/notifications")
public class NotificationListController {
    private final NotificationListBloc notificationListBloc;

    public NotificationListController(NotificationListBloc notificationListBloc) {
        this.notificationListBloc = notificationListBloc;
    }


    @GetMapping
    private ResponseEntity<?> fetchAdminNotifications(final BasePaginationReq basePaginationReq){
        Page<Notification> notifications = notificationListBloc.fetchAdminNotifications(basePaginationReq);
        return ResponseEntity.ok(
                PaginationHelper.buildBasePaginationRes(
                        notifications.map(NotificationMapper.INSTANCE::toNotificationRes)));
    }
}
