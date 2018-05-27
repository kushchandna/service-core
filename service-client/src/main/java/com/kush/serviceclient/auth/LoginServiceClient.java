package com.kush.serviceclient.auth;

import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.serviceclient.ServiceClient;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ResultListener;

public class LoginServiceClient extends ServiceClient {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(LoginServiceClient.class);

    public LoginServiceClient() {
        super("com.kush.service.auth.LoginService");
    }

    public Response<User> register(Credential credential) {
        return invoke("register", credential);
    }

    public Response<AuthToken> login(Credential credential) {
        Response<AuthToken> response = invoke("login", credential);
        response.addResultListener(new ResultListener<AuthToken>() {

            @Override
            public void onResult(AuthToken result) {
                LOGGER.info("Received token for user id %s", result.getUser().getId());
                SessionManager sessionManager = getSessionManager();
                sessionManager.startSession(result);
            }
        });
        return response;
    }

    public Response<Void> logout() {
        Response<Void> response = authInvoke("logout");
        response.addResultListener(new ResultListener<Void>() {

            @Override
            public void onResult(Void result) {
                SessionManager sessionManager = getSessionManager();
                sessionManager.endSession();
                LOGGER.info("Logged out");
            }
        });
        return response;
    }
}
