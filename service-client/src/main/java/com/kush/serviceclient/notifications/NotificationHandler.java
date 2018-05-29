package com.kush.serviceclient.notifications;

import com.kush.lib.service.remoting.notifications.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
