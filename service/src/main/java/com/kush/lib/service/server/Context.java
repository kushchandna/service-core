package com.kush.lib.service.server;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<Object, Object> instances = new HashMap<>();

    Context() {
    }

    Context(Map<Object, Object> instances) {
        this.instances.putAll(instances);
    }

    void addInstance(Object key, Object value) {
        addInstance(instances, key, value);
    }

    public boolean containsKey(Object key) {
        return instances.containsKey(prepareInstanceKey(key));
    }

    public <T> T getInstance(Object key, Class<T> returnType, T defaultValue) {
        if (!containsKey(key)) {
            return defaultValue;
        }
        return returnType.cast(getInstanceValue(instances, key));
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

    static void addInstance(Map<Object, Object> instances, Object key, Object value) {
        instances.put(prepareInstanceKey(key), value);
    }

    private static String prepareInstanceKey(Object key) {
        return key.toString();
    }

    private static Object getInstanceValue(Map<Object, Object> instances, Object key) {
        return instances.get(prepareInstanceKey(key));
    }
}
