package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;

@Service(name = "Login")
public class LoginService extends BaseService {

    @ServiceMethod(name = "login")
    public AuthToken login(Credential credential) {
        return null;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "logout")
    public void logout() {
    }
}
