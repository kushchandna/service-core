package com.kush.serviceclient.notifications;

import com.kush.lib.service.remoting.notifications.Notification;
import com.kush.serviceclient.ApplicationClient;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;

public class NotifiableApplicationClient extends ApplicationClient {

    private final ResolutionRequestsReceiver<Notification> notificationsReceiver;

    public NotifiableApplicationClient(ResolutionRequestsReceiver<Notification> notificationsReceiver) {
        this.notificationsReceiver = notificationsReceiver;
    }

    public void startNotificationsReceiver(NotificationHandler notificationHandler) throws StartupFailedException {
        notificationsReceiver.start(notificationHandler);
    }
}
