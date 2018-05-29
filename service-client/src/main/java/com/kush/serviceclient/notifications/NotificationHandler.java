package com.kush.serviceclient.notifications;

import com.kush.lib.service.remoting.notifications.Notification;
import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.server.Resolver;

public abstract class NotificationHandler implements Resolver<Notification> {

    @Override
    public Object resolve(Notification notification) throws ResolutionFailedException {
        handle(notification);
        return null;
    }

    protected abstract void handle(Notification notification);
}
