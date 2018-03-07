package com.kush.lib.persistence.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kush.lib.persistence.api.Persistor;
import com.kush.utils.id.IdGenerator;
import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public abstract class InMemoryPersistor<T extends Identifiable> implements Persistor<T> {

    private final Map<Identifier, T> savedObjects = new HashMap<>();

    private final IdGenerator idGenerator;

    public InMemoryPersistor(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public T save(T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Identifier id = object.getId();
        if (id == null) {
            throw new NullPointerException();
        }
        T persistableObject;
        if (Identifier.NULL.equals(id)) {
            id = idGenerator.next();
            persistableObject = createPersistableObject(id, object);
        } else {
            persistableObject = object;
        }
        savedObjects.put(id, persistableObject);
        return persistableObject;
    }

    @Override
    public T fetch(Identifier id) {
        return savedObjects.get(id);
    }

    @Override
    public Iterator<T> fetch(Collection<Identifier> ids) {
        List<T> objects = new ArrayList<>();
        for (Identifier id : ids) {
            T object = fetch(id);
            objects.add(object);
        }
        return objects.iterator();
    }

    @Override
    public Iterator<T> fetchAll() {
        return savedObjects.values().iterator();
    }

    @Override
    public boolean remove(Identifier id) {
        return savedObjects.remove(id) != null;
    }

    protected abstract T createPersistableObject(Identifier id, T reference);
}
