package com.kush.lib.service.remoting;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.kush.utils.id.Identifier;

public interface Notification extends Serializable {

    Identifier getId();

    Identifier getUserId();

    Serializable getData();

    boolean isRead();

    LocalDateTime getNotificationTime();

    String getSource();

}