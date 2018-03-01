package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;

class ThreadBasedAuth implements Auth {

    private final ThreadLocal<AuthToken> CURRENT = new ThreadLocal<>();

    @Override
    public void login(AuthToken token) {
        CURRENT.set(token);
    }

    @Override
    public void logout() {
        CURRENT.remove();
    }

    @Override
    public User getCurrentUser() {
        AuthToken token = CURRENT.get();
        return token == null ? null : token.getUser();
    }
}
