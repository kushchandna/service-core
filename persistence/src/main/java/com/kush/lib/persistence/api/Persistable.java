package com.kush.lib.persistence.api;

public class Persistable {

    private final Identifier id;

    public Persistable() {
        this(Identifier.NULL);
    }

    public Persistable(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }
}
