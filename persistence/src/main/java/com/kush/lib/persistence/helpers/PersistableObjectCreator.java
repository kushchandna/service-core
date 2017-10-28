package com.kush.lib.persistence.helpers;

import com.kush.lib.persistence.api.Identifier;
import com.kush.lib.persistence.api.Persistable;

public interface PersistableObjectCreator<T extends Persistable> {

    T create(Identifier id, T reference);
}
