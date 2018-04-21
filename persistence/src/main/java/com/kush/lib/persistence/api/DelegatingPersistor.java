package com.kush.lib.persistence.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class DelegatingPersistor<T extends Identifiable> implements Persistor<T> {

    private final Persistor<T> delegate;

    public DelegatingPersistor(Persistor<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T save(T object) throws PersistorOperationFailedException {
        return delegate.save(object);
    }

    @Override
    public T fetch(Identifier id) throws PersistorOperationFailedException {
        return delegate.fetch(id);
    }

    @Override
    public List<T> fetch(Predicate<T> filter, Comparator<T> order, int count) throws PersistorOperationFailedException {
        return delegate.fetch(filter, order, count);
    }

    @Override
    public List<T> fetch(Predicate<T> filter) throws PersistorOperationFailedException {
        return delegate.fetch(filter);
    }

    @Override
    public List<T> fetchAll() throws PersistorOperationFailedException {
        return delegate.fetchAll();
    }

    @Override
    public boolean remove(Identifier id) throws PersistorOperationFailedException {
        return delegate.remove(id);
    }
}
