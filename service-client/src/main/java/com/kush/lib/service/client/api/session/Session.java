package com.kush.lib.service.client.api.session;

import com.kush.lib.service.remoting.auth.AuthToken;

public class Session {

    private final AuthToken token;

    public Session(AuthToken token) {
        this.token = token;
    }

    public AuthToken getToken() {
        return token;
    }
}
