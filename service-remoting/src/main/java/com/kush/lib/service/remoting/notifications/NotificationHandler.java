package com.kush.lib.service.remoting.notifications;

import com.kush.utils.signaling.SignalHandler;

public interface NotificationHandler extends SignalHandler {

    void onNotificationAdded(Notification notification);
}
