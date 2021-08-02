package com.kush.service.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;

public class DefaultNotificationPersistor extends DelegatingPersister<PersistableNotification> implements NotificationPersister {

    public DefaultNotificationPersistor(Persister<PersistableNotification> delegate) {
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
