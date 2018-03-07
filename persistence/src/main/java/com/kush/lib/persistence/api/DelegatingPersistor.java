package com.kush.lib.persistence.api;

import java.util.Collection;
import java.util.Iterator;

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
    public Iterator<T> fetch(Collection<Identifier> ids) throws PersistorOperationFailedException {
        return delegate.fetch(ids);
    }

    @Override
    public Iterator<T> fetchAll() throws PersistorOperationFailedException {
        return delegate.fetchAll();
    }

    @Override
    public boolean remove(Identifier id) throws PersistorOperationFailedException {
        return delegate.remove(id);
    }
}
