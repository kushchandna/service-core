package com.kush.lib.service.server.api;

import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    private final Map<Object, Object> instances = new HashMap<>();

    private ContextBuilder() {
    }

    public static ContextBuilder create() {
        return new ContextBuilder();
    }

    public ContextBuilder withInstance(Object key, Object instance) {
        instances.put(key, instance);
        return this;
    }

    public Context build() {
        return new Context(instances);
    }
}
