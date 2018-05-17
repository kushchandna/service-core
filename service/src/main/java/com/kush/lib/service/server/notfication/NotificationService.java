package com.kush.lib.service.server.notfication;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.service.remoting.Notification;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;
import com.kush.utils.id.Identifier;

@Service
public class NotificationService extends BaseService {

    @ServiceMethod
    public void sendNotification(Identifier userId, Serializable data, String source) throws PersistorOperationFailedException {
        NotificationPersistor notificationPersistor = getInstance(NotificationPersistor.class);
        LocalDateTime notificationTime = LocalDateTime.now(getInstance(Clock.class));
        notificationPersistor.addNotification(userId, data, false, notificationTime, source);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<? extends Notification> getUnreadNotifications() throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        NotificationPersistor notificationPersistor = getInstance(NotificationPersistor.class);
        return notificationPersistor.getRecentUnreadNotificationsForUser(currentUserId);
    }

    @AuthenticationRequired
    @ServiceMethod
    public Notification getNotification() {
        return null;
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(NotificationPersistor.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }
}
