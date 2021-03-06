package com.kush.service;

import static java.lang.String.format;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.auth.Auth;
import com.kush.service.auth.AuthenticationFailedException;
import com.kush.service.auth.SessionManager;

public abstract class BaseService {

    private static final Logger LOGGER = LogManager.getFormatterLogger(BaseService.class);

    private Context context;

    synchronized final void initialize(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Service already initialized");
        }
        context.addInstance(getClass(), this);
        this.context = context;
        processContext();
        LOGGER.info("Initialized service %s", getClass().getName());
    }

    protected synchronized final Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Service not initialized yet");
        }
        return context;
    }

    protected void processContext() {
    }

    protected final void addIfDoesNotExist(Object key, Object instance) {
        if (!contextContains(key)) {
            enrichContext(key, instance);
        }
    }

    protected final boolean contextContains(Object key) {
        return context.containsKey(key);
    }

    protected void enrichContext(Object key, Object instance) {
        context.addInstance(key, instance);
        LOGGER.info("Added %s to context", key);
    }

    protected final void checkContextHasValueFor(Object key) {
        if (!contextContains(key)) {
            String error = format("%s service requires %s in its context.", getClass().getName(), key);
            LOGGER.error(error);
            throw new IllegalStateException(error);
        }
    }

    protected final void checkSessionActive() throws AuthenticationFailedException {
        getCurrentUser();
    }

    protected final User getCurrentUser() throws AuthenticationFailedException {
        Auth authenticator = context.getInstance(Auth.class);
        AuthToken token = authenticator.getToken();
        if (token == null) {
            throw new AuthenticationFailedException("Authentication required to perform this action");
        }
        User currentUser = token.getUser();
        SessionManager sessionManager = context.getInstance(SessionManager.class);
        if (!sessionManager.checkSessionExists(currentUser)) {
            throw new AuthenticationFailedException("No active session found for user with id " + currentUser.getId());
        }
        return currentUser;
    }

    protected final <T> T getInstance(Class<T> type) {
        return getContext().getInstance(type);
    }
}
