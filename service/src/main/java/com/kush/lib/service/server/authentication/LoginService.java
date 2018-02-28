package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.credential.Credential;

@Service(name = "Login")
public class LoginService extends BaseService {

    @ServiceMethod(name = "register")
    public User register(Credential credential) throws UserRegistrationFailedException {
        return null;
    }

    @ServiceMethod(name = "login")
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        return null;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "logout")
    public void logout() {
    }
}
