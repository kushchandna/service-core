package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.ServiceApi;

public abstract class BaseService implements ServiceApi {

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
}
