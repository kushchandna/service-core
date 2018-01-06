package com.kush.lib.service.server.api;

import java.util.Map;

public class Context {

    private final Map<Object, Object> instances;

    Context(Map<Object, Object> instances) {
        this.instances = instances;
    }

    public <T> T getInstance(Class<T> typeClass) {
        return getInstance(typeClass, typeClass);
    }

    public <T> T getInstance(Object key, Class<T> typeClass) {
        if (instances.containsKey(key)) {
            return typeClass.cast(instances.get(key));
        }
        throw new IllegalArgumentException("No instance found for key " + key);
    }
}
