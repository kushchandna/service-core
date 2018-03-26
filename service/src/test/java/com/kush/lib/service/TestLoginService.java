package com.kush.lib.service;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.authentication.AuthenticationFailedException;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.lib.service.server.authentication.UserRegistrationFailedException;

public class TestLoginService extends LoginService {

    private final ServiceRequestResolver serviceRequestResolver;

    public TestLoginService(TestApplicationServer server) {
        serviceRequestResolver = server.getServiceRequestResolver();
    }

    @Override
    public User register(Credential credential) throws UserRegistrationFailedException {
        return super.register(credential);
    }

    @Override
    public AuthToken login(Credential credential) throws AuthenticationFailedException {
        return super.login(credential);
    }

    @Override
    public void logout() {
        super.logout();
    }
}
