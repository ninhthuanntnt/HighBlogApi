package com.high.highblog.api.admin;

import com.high.highblog.bloc.admin.NotificationCrudBloc;
import com.high.highblog.model.dto.request.admin.NotificationCreateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("adminNotificationCrudController")
@RequestMapping("/api/v1/admin/notifications")
public class NotificationCrudController {
    private final NotificationCrudBloc notificationCrudBloc;

    public NotificationCrudController(final NotificationCrudBloc notificationCrudBloc) {
        this.notificationCrudBloc = notificationCrudBloc;
    }

    @PostMapping
    private ResponseEntity<?> createNotification(@Valid @RequestBody
                                                        final NotificationCreateReq notificationCreateReq) {

        notificationCrudBloc.createNotification(notificationCreateReq);

        return ResponseEntity.noContent().build();
    }
}
