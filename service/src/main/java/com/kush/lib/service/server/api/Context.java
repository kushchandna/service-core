package com.kush.lib.service.server.api;

import java.util.Map;

public class Context {

    private final Map<Object, Object> instances;

    Context(Map<Object, Object> instances) {
        this.instances = instances;
    }

    public <T> T getInstance(Object key, Class<T> returnType, T defaultValue) {
        if (!instances.containsKey(key)) {
            return defaultValue;
        }
        return returnType.cast(instances.get(key));
    }

    public <T> T getInstance(Object key, Class<T> returnType) {
        return getInstance(key, returnType, null);
    }

    public <T> T getInstance(Class<T> returnType, T defaultValue) {
        return getInstance(returnType, returnType, defaultValue);
    }

    public <T> T getInstance(Class<T> returnType) {
        return getInstance(returnType, returnType, null);
    }
}
