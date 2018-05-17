package com.kush.lib.service.server.notfication;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.lib.service.remoting.Notification;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

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

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#getId()
     */
    @Override
    public Identifier getId() {
        return notificationId;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#getUserId()
     */
    @Override
    public Identifier getUserId() {
        return userId;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#getData()
     */
    @Override
    public Serializable getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#isRead()
     */
    @Override
    public boolean isRead() {
        return read;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#getNotificationTime()
     */
    @Override
    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.kush.lib.service.server.notfication.Notification#getSource()
     */
    @Override
    public String getSource() {
        return source;
    }

    void markAsRead() {
        read = true;
    }
}
