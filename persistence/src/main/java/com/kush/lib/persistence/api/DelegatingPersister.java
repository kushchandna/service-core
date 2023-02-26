package com.kush.lib.persistence.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;

public class DelegatingPersister<T extends Identifiable> implements Persister<T> {

    private final Persister<T> delegate;

    public DelegatingPersister(Persister<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T save(T object) throws PersistenceOperationFailedException {
        return delegate.save(object);
    }

    @Override
    public T fetch(Identifier id) throws PersistenceOperationFailedException {
        return delegate.fetch(id);
    }

    @Override
    public List<T> fetch(Predicate<T> filter, Comparator<T> order, int count) throws PersistenceOperationFailedException {
        return delegate.fetch(filter, order, count);
    }

    @Override
    public List<T> fetch(Predicate<T> filter) throws PersistenceOperationFailedException {
        return delegate.fetch(filter);
    }

    @Override
    public List<T> fetchAll() throws PersistenceOperationFailedException {
        return delegate.fetchAll();
    }

    @Override
    public boolean remove(Identifier id) throws PersistenceOperationFailedException {
        return delegate.remove(id);
    }
}
