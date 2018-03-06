package com.kush.lib.service.server;

import java.util.Map;

import com.kush.lib.persistence.api.Persistor;
import com.kush.utils.id.Identifiable;

public class Context {

    private static final String PREFIX_INSTANCE = "INSTANCE_";
    private static final String PREFIX_PERSISTOR = "PERSISTOR_";

    private final Map<Object, Object> instances;

    Context(Map<Object, Object> instances) {
        this.instances = instances;
    }

    void addInstance(Object key, Object value) {
        addInstance(instances, key, value);
    }

    void addPersistor(Object key, Object value) {
        addPersistor(instances, key, value);
    }

    public boolean containsKey(Object key) {
        return instances.containsKey(prepareInstanceKey(key));
    }

    public boolean containsPersistor(Class<? extends Identifiable> key) {
        return instances.containsKey(preparePersistorKey(key));
    }

    @SuppressWarnings("unchecked")
    public <T extends Identifiable> Persistor<T> getPersistor(Class<T> type) {
        return (Persistor<T>) getPersistorValue(instances, type);
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

    static void addPersistor(Map<Object, Object> instances, Object key, Object value) {
        instances.put(preparePersistorKey(key), value);
    }

    private static String prepareInstanceKey(Object key) {
        return PREFIX_INSTANCE + key;
    }

    private static String preparePersistorKey(Object key) {
        return PREFIX_PERSISTOR + key;
    }

    private static Object getInstanceValue(Map<Object, Object> instances, Object key) {
        return instances.get(prepareInstanceKey(key));
    }

    private static Object getPersistorValue(Map<Object, Object> instances, Object key) {
        return instances.get(preparePersistorKey(key));
    }
}
