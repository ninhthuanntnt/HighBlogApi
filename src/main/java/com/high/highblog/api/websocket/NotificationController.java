package com.high.highblog.api.websocket;

import com.high.highblog.bloc.notification.NotificationBloc;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller("websocketNotificationController")
public class NotificationController {
    private final NotificationBloc notificationBloc;


    public NotificationController(final NotificationBloc notificationBloc) {
        this.notificationBloc = notificationBloc;
    }

    @SubscribeMapping("/exchange/amq.direct/notification")
    public void sendInitNotification() {
        notificationBloc.pushUnreadNotificationToCurrentUser();
    }

}
