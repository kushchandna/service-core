package com.kush.lib.service.client.api.notification;

import com.kush.lib.service.remoting.Notification;

public interface NotificationHandler {

    void handle(Notification notification);
}
