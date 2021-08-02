package com.kush.service.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kush.commons.id.Identifier;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;

public class SessionManager {

    private static final Object DUMMY = new Object();

    private final Map<Identifier, Object> activeUsers = new ConcurrentHashMap<>();

    public AuthToken startSession(User user) {
        activeUsers.put(user.getId(), DUMMY);
        return new AuthToken(user);
    }

    public void endSession(User user) {
        activeUsers.remove(user.getId());
    }

    public boolean checkSessionExists(User user) {
        return activeUsers.containsKey(user.getId());
    }
}

