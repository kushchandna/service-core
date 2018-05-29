package com.kush.lib.service.remoting.notifications;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.utils.id.Identifier;
import com.kush.utils.remoting.Resolvable;

public interface Notification extends Resolvable {

    Identifier getId();

    Identifier getUserId();

    Serializable getData();

    boolean isRead();

    LocalDateTime getNotificationTime();

    String getSource();

}