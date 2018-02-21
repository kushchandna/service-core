package com.kush.lib.service.server;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;

public interface Auth {

    public static final Auth DEFAULT = new ThreadBasedAuth();

    void login(AuthToken token);

    void logout();

    User getCurrentUser();
}
