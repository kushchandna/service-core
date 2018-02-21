package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;

public interface Authenticator {

    public static final Authenticator OPEN = new OpenAuthenticator();

    void login(AuthToken token);

    void logout();

    User getCurrentUser();

    static final class OpenAuthenticator implements Authenticator {

        @Override
        public void logout() {
        }

        @Override
        public void login(AuthToken token) {
        }

        @Override
        public User getCurrentUser() {
            return null;
        }
    }
}
