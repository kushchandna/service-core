package com.kush.lib.persistence.helpers;

import com.kush.lib.persistence.api.Persistable;
import com.kush.lib.service.api.Identifier;

public interface PersistableObjectCreator<T extends Persistable> {

    T create(Identifier id, T reference);
}
