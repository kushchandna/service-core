package com.kush.lib.service.remoting.auth;

import java.io.Serializable;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class User implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private final Identifier userId;

    public User(Identifier userId) {
        this.userId = userId;
    }

    @Override
    public Identifier getId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User - " + userId;
    }
}
