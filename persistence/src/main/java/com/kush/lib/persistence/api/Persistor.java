package com.kush.lib.persistence.api;

import java.util.Collection;
import java.util.Iterator;

public interface Persistor<T extends Persistable> {

    T save(T object);

    T fetch(Identifier id);

    Iterator<T> fetch(Collection<Identifier> ids);

    Iterator<T> fetchAll();

    boolean remove(Identifier id);
}
