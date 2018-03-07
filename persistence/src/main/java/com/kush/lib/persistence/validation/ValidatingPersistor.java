package com.kush.lib.persistence.validation;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifiable;

public abstract class ValidatingPersistor<T extends Identifiable> extends DelegatingPersistor<T> {

    public ValidatingPersistor(Persistor<T> delegate) {
        super(delegate);
    }

    @Override
    public T save(T object) throws PersistorOperationFailedException {
        validate(object);
        return super.save(object);
    }

    protected abstract void validate(T object) throws PersistorOperationFailedException;
}
