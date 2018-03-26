package com.kush.lib.service.server;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.AuthenticationFailedException;
import com.kush.lib.service.server.authentication.SessionManager;
import com.kush.utils.id.Identifiable;

public abstract class BaseService {

    private static final com.kush.logger.Logger LOGGER = com.kush.logger.LoggerFactory.INSTANCE.getLogger(BaseService.class);

    private Context context;

    public synchronized final void initialize(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Service already initialized");
        }
        this.context = context;
        LOGGER.info("Initialized service %s", getClass().getName());
    }

    protected Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Service not initialized yet");
        }
        return context;
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

    protected final <T extends Identifiable> Persistor<T> getPersistor(Class<T> type) {
        return getContext().getPersistor(type);
    }

    protected final <T> T getInstance(Class<T> type) {
        return getContext().getInstance(type);
    }
}
