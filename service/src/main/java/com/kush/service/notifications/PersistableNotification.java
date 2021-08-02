package com.kush.service.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.service.remoting.notifications.Notification;

public class PersistableNotification implements Identifiable, Notification {

    private static final long serialVersionUID = 1L;

    private final Identifier notificationId;
    private final Identifier userId;
    private final Serializable data;
    private final LocalDateTime notificationTime;
    private final String source;

    private boolean read;

    public PersistableNotification(Identifier userId, Serializable data, boolean read, LocalDateTime notificationTime,
            String source) {
        this(Identifier.NULL, userId, data, read, notificationTime, source);
    }

    public PersistableNotification(Identifier notificationId, Notification notification) {
        this(notificationId, notification.getUserId(), notification.getData(), notification.isRead(),
                notification.getNotificationTime(), notification.getSource());
    }

    public PersistableNotification(Identifier notificationId, Identifier userId, Serializable data, boolean read,
            LocalDateTime notificationTime, String source) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.data = data;
        this.read = read;
        this.notificationTime = notificationTime;
        this.source = source;
    }

    @Override
    public Identifier getId() {
        return notificationId;
    }

    @Override
    public Identifier getUserId() {
        return userId;
    }

    @Override
    public Serializable getData() {
        return data;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    @Override
    public String getSource() {
        return source;
    }

    void markAsRead() {
        read = true;
    }
}
