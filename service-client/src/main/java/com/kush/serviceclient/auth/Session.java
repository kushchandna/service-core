package com.kush.serviceclient.auth;

import com.kush.lib.service.remoting.auth.AuthToken;

public class Session {

    private final AuthToken token;

    public Session(AuthToken token) {
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }

    @Override
    public String toString() {
        return String.format("Session [userId: %s]", token.getUser().getId());
    }
}
