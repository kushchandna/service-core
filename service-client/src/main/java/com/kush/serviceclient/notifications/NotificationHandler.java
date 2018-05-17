package com.kush.serviceclient.notifications;

import com.kush.lib.service.remoting.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
