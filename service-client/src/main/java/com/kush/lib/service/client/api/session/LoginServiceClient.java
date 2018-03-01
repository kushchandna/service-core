package com.kush.lib.service.client.api.session;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.utils.async.Response;

public class LoginServiceClient extends ServiceClient {

    public LoginServiceClient() {
        super("Login");
    }

    public Response<User> register(Credential credential) {
        return invoke("register", credential);
    }

    public Response<AuthToken> login(Credential credential) {
        return invoke("register", credential);
    }

    public Response<Void> logout() {
        return invoke("logout");
    }
}
