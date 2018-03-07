package com.kush.lib.persistence.helpers;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class PersistableObject<T> implements Identifiable {

    private final Identifier id;
    private final T object;

    public PersistableObject(T object) {
        this(Identifier.NULL, object);
    }

    public PersistableObject(Identifier id, T object) {
        this.id = id;
        this.object = object;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public T get() {
        return object;
    }
}
