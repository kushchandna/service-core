package com.kush.service.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface NotificationPersistor extends Persistor<PersistableNotification> {

    PersistableNotification addNotification(Identifier userId, Serializable data, boolean read, LocalDateTime notificationTime,
            String source) throws PersistorOperationFailedException;

    List<PersistableNotification> getRecentUnreadNotificationsForUser(Identifier userId) throws PersistorOperationFailedException;
}
