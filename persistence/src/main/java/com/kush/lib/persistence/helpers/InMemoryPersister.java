package com.kush.lib.persistence.helpers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.kush.commons.id.IdGenerator;
import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.commons.id.SequentialIdGenerator;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;

public class InMemoryPersister<T extends Identifiable> implements Persister<T> {

    private final Map<Identifier, T> savedObjects = new LinkedHashMap<>();

    private final Class<T> type;
    private final IdGenerator idGenerator;

    public InMemoryPersister(Class<T> type) {
        this(type, new SequentialIdGenerator());
    }

    public InMemoryPersister(Class<T> type, IdGenerator idGenerator) {
        this.type = type;
        this.idGenerator = idGenerator;
    }

    public static <T extends Identifiable> Persister<T> forType(Class<T> type) {
        return new InMemoryPersister<>(type);
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
    public List<T> fetch(Predicate<T> filter, Comparator<T> order, int count) throws PersistenceOperationFailedException {
        return savedObjects.values().stream().filter(filter).sorted(order).limit(count == -1 ? Integer.MAX_VALUE : count)
            .collect(Collectors.toList());
    }

    @Override
    public List<T> fetch(Predicate<T> filter) throws PersistenceOperationFailedException {
        return savedObjects.values().stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public List<T> fetchAll() {
        return Collections.unmodifiableList(new ArrayList<>(savedObjects.values()));
    }

    @Override
    public boolean remove(Identifier id) {
        return savedObjects.remove(id) != null;
    }

    protected T createPersistableObject(Identifier id, T reference) {
        try {
            Constructor<T> constructor = type.getConstructor(Identifier.class, type);
            return constructor.newInstance(id, reference);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
