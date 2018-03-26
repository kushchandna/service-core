package com.kush.lib.service.server;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.AuthenticationFailedException;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.lib.service.server.authentication.UserRegistrationFailedException;

public class TestLoginService extends LoginService {

    @Override
    public User register(Credential credential) throws UserRegistrationFailedException {
        return super.register(credential);
    }

    @Override
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        AuthToken token = super.login(credential);
        Auth auth = getInstance(Auth.class);
        auth.login(token);
        return token;
    }

    @Override
    public void logout() {
        Auth auth = getInstance(Auth.class);
        auth.logout();
        super.logout();
    }
}
