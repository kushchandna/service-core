package com.kush.lib.service.client.api.notification;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.remoting.Notification;
import com.kush.utils.async.Response;

public class NotificationClient extends ServiceClient {

    private Response<Notification> lastNotificationResponse;

    public NotificationClient(String serviceName) {
        super("com.kush.service.notification.NotificationService");
    }

    public synchronized void startListeningNotification() {
        readNextNotification();
    }

    public synchronized void stopListeningNotifications() {
        if (lastNotificationResponse != null) {
            lastNotificationResponse.removeListeners();
        }
    }

    private void readNextNotification() {
        lastNotificationResponse = invoke("getNotification");
        lastNotificationResponse.addResultListener((anyNotification) -> {
            readNextNotification();
        });
        lastNotificationResponse.addErrorListener((anyError) -> {
            readNextNotification();
        });
    }
}
