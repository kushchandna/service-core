package com.kush.lib.service.remoting.auth;

import java.io.Serializable;

public class AuthToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User user;

    public AuthToken(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Token [user: %s]", user.getId());
    }
}
