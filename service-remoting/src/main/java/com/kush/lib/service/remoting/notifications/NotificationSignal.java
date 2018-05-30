package com.kush.lib.service.remoting.notifications;

import com.kush.utils.signaling.Signal;

public class NotificationSignal extends Signal<NotificationHandler> {

    private static final long serialVersionUID = 1L;

    private final Notification notification;

    public NotificationSignal(Notification notification) {
        this.notification = notification;
    }

    @Override
    protected void handleSignal(NotificationHandler handler) {
        handler.onNotificationAdded(notification);
    }
}
