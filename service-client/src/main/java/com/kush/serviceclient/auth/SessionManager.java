package com.kush.serviceclient.auth;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.auth.AuthToken;

public class SessionManager {

    private static final Logger LOGGER = LogManager.getFormatterLogger(SessionManager.class);

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
