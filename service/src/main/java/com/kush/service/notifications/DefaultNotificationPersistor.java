package com.kush.service.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultNotificationPersistor extends DelegatingPersistor<PersistableNotification> implements NotificationPersistor {

    public DefaultNotificationPersistor(Persistor<PersistableNotification> delegate) {
        super(delegate);
    }

    @Override
    public PersistableNotification addNotification(Identifier userId, Serializable data, boolean read,
            LocalDateTime notificationTime, String source) throws PersistorOperationFailedException {
        PersistableNotification notification = new PersistableNotification(userId, data, read, notificationTime, source);
        return save(notification);
    }

    @Override
    public List<PersistableNotification> getRecentUnreadNotificationsForUser(Identifier userId)
            throws PersistorOperationFailedException {
        return fetch(notification -> notification.getUserId().equals(userId) && !notification.isRead(), (n1, n2) -> {
            return n1.getNotificationTime().isAfter(n2.getNotificationTime()) ? -1 : 1;
        }, -1);
    }
}
