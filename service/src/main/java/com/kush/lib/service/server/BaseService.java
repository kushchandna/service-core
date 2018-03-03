package com.kush.lib.service.server;

import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.AuthenticationFailedException;

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

    protected final Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Service not initialized yet");
        }
        return context;
    }

    protected final User getCurrentUser() throws AuthenticationFailedException {
        Auth authenticator = context.getInstance(Auth.class);
        User currentUser = authenticator.getCurrentUser();
        if (currentUser == null) {
            throw new AuthenticationFailedException("No login");
        }
        return currentUser;
    }
}
