package com.kush.lib.service.client.api.session;

import java.util.concurrent.atomic.AtomicReference;

import com.kush.lib.service.remoting.auth.AuthToken;

public class SessionManager {

    private final AtomicReference<Session> currentSession = new AtomicReference<>();

    public Session getCurrentSession() {
        return currentSession.get();
    }

    public void startSession(AuthToken token) {
        currentSession.set(new Session(token));
    }
}
