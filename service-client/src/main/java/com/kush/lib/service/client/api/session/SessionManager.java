package com.kush.lib.service.client.api.session;

import java.util.concurrent.atomic.AtomicReference;

import com.kush.lib.service.remoting.auth.AuthToken;

public class SessionManager {

    private static final com.kush.logger.Logger LOGGER = com.kush.logger.LoggerFactory.INSTANCE.getLogger(SessionManager.class);

    private final AtomicReference<Session> currentSession = new AtomicReference<>();

    public Session getCurrentSession() {
        return currentSession.get();
    }

    public void startSession(AuthToken token) {
        Session session = new Session(token);
        currentSession.set(session);
        LOGGER.info("Started session %s", session);
    }

    public void endSession() {
        Session oldSession = currentSession.getAndSet(null);
        LOGGER.info("Ended session %s", oldSession);
    }
}
