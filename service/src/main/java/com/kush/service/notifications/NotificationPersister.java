package com.kush.service.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.kush.commons.id.Identifier;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public interface NotificationPersister extends Persister<PersistableNotification> {

    PersistableNotification addNotification(Identifier userId, Serializable data, boolean read, LocalDateTime notificationTime,
            String source) throws PersistenceOperationFailedException;

    List<PersistableNotification> getRecentUnreadNotificationsForUser(Identifier userId) throws PersistenceOperationFailedException;
}
