package com.kush.lib.service.server;

import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.Authenticator;

public abstract class BaseService {

    private Context context;

    public synchronized final void initialize(Context context) {
        if (this.context != null) {
            throw new IllegalStateException("Service already initialized");
        }
        this.context = context;
    }

    protected final Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Service not initialized yet");
        }
        return context;
    }

    protected final User getCurrentUser() {
        Authenticator authenticator = context.getInstance(Authenticator.class);
        return authenticator.getCurrentUser();
    }
}
