package com.kush.lib.persistence.api;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public interface Persistor<T extends Identifiable> {

    T save(T object) throws PersistorOperationFailedException;

    T fetch(Identifier id) throws PersistorOperationFailedException;

    Iterator<T> fetch(Predicate<T> filter, Comparator<T> order, int count) throws PersistorOperationFailedException;

    Iterator<T> fetch(Predicate<T> filter) throws PersistorOperationFailedException;

    Iterator<T> fetchAll() throws PersistorOperationFailedException;

    boolean remove(Identifier id) throws PersistorOperationFailedException;
}
