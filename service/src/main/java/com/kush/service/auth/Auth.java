package com.kush.service.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.auth.AuthToken;

public class Auth {

    private static final Logger LOGGER = LogManager.getFormatterLogger(Auth.class);

    private final ThreadLocal<AuthToken> CURRENT = new ThreadLocal<>();

    public void login(AuthToken token) {
        CURRENT.set(token);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Added authentication context with token %s for thread %s", token, Thread.currentThread().getName());
        }
    }

    public void logout() {
        CURRENT.remove();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Cleared authentication context for thread %s", Thread.currentThread().getName());
        }
    }

    public AuthToken getToken() {
        return CURRENT.get();
    }
}
