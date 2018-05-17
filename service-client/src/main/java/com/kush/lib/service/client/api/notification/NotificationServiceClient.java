package com.kush.lib.service.client.api.notification;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.remoting.Notification;
import com.kush.utils.async.Response;

public class NotificationServiceClient extends ServiceClient {

    private Response<Notification> lastNotificationResponse;

    public NotificationServiceClient(String serviceName) {
        super("com.kush.service.notification.NotificationService");
    }

    public synchronized void startListeningNotification(NotificationHandler notificationHandler) {
        readNextNotification(notificationHandler);
    }

    public synchronized void stopListeningNotifications() {
        if (lastNotificationResponse != null) {
            lastNotificationResponse.removeListeners();
        }
    }

    private void readNextNotification(NotificationHandler notificationHandler) {
        lastNotificationResponse = invoke("getNotification");
        lastNotificationResponse.addResultListener((notification) -> {
            notificationHandler.handle(notification);
        });
        lastNotificationResponse.addResultListener((anyNotification) -> {
            readNextNotification(notificationHandler);
        });
        lastNotificationResponse.addErrorListener((anyError) -> {
            readNextNotification(notificationHandler);
        });
    }
}
