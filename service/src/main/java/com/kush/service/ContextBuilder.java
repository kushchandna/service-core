package com.kush.service;

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
        Context.addInstance(instances, key, instance);
        return this;
    }

    public Context build() {
        return new Context(instances);
    }
}
