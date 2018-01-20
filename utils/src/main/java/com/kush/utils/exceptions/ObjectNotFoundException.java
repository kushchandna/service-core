package com.kush.utils.exceptions;

public class ObjectNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String typeName, Object objectName) {
        super(String.format("No %s found with name %s", typeName, objectName));
    }
}
