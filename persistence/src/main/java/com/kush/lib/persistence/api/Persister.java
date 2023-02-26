package com.kush.lib.persistence.api;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;

public interface Persister<T extends Identifiable> {

    T save(T object) throws PersistenceOperationFailedException;

    T fetch(Identifier id) throws PersistenceOperationFailedException;

    List<T> fetch(Predicate<T> filter, Comparator<T> order, int count) throws PersistenceOperationFailedException;

    List<T> fetch(Predicate<T> filter) throws PersistenceOperationFailedException;

    List<T> fetchAll() throws PersistenceOperationFailedException;

    boolean remove(Identifier id) throws PersistenceOperationFailedException;
}
