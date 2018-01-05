package com.kush.lib.persistence.api;

import java.util.Collection;
import java.util.Iterator;

import com.kush.lib.service.api.Identifier;

public interface Persistor<T extends Persistable> {

    T save(T object);

    T fetch(Identifier id);

    Iterator<T> fetch(Collection<Identifier> ids);

    Iterator<T> fetchAll();

    boolean remove(Identifier id);
}
