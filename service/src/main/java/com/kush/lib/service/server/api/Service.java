package com.kush.lib.service.server.api;

public abstract class Service {

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
