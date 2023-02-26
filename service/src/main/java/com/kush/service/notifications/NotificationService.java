package com.kush.service.notifications;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.lib.service.remoting.notifications.Notification;
import com.kush.service.BaseService;
import com.kush.service.annotations.Service;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.AuthenticationRequired;

@Service
public class NotificationService extends BaseService {

    @ServiceMethod
    public void sendNotification(Identifier userId, Serializable data, String source) throws PersistenceOperationFailedException {
        NotificationPersister notificationPersistor = getInstance(NotificationPersister.class);
        LocalDateTime notificationTime = LocalDateTime.now(getInstance(Clock.class));
        notificationPersistor.addNotification(userId, data, false, notificationTime, source);
    }

    @AuthenticationRequired
    @ServiceMethod
    public List<? extends Notification> getUnreadNotifications() throws PersistenceOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        NotificationPersister notificationPersistor = getInstance(NotificationPersister.class);
        return notificationPersistor.getRecentUnreadNotificationsForUser(currentUserId);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(NotificationPersister.class);
        addIfDoesNotExist(Clock.class, Clock.systemUTC());
    }
}
