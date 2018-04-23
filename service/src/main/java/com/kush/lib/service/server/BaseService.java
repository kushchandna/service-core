package com.kush.lib.service.server;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.AuthenticationFailedException;
import com.kush.lib.service.server.authentication.SessionManager;

public abstract class BaseService {

    private static final com.kush.logger.Logger LOGGER = com.kush.logger.LoggerFactory.INSTANCE.getLogger(BaseService.class);

    private Context context;

    synchronized final void initialize(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Service already initialized");
        }
        context.addInstance(getClass(), this);
        this.context = context;
        LOGGER.info("Initialized service %s", getClass().getName());
    }

    protected synchronized final Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Service not initialized yet");
        }
        return context;
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
