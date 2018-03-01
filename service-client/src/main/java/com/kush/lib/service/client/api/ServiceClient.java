package com.kush.lib.service.client.api;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.session.Session;
import com.kush.lib.service.client.api.session.SessionManager;
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.ServiceConnectionFailedException;
import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Responder;
import com.kush.utils.async.Response;

public abstract class ServiceClient {

    private final String serviceName;

    private Responder responder;
    private ServiceConnectionFactory connectionFactory;
    private SessionManager sessionManager;

    public ServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    public void activate(Executor executor, ServiceConnectionFactory connectionFactory, SessionManager sessionManager) {
        this.connectionFactory = connectionFactory;
        this.sessionManager = sessionManager;
        responder = new Responder(executor);
    }

    protected final SessionManager getSessionManager() {
        return sessionManager;
    }

    protected final <T> Response<T> invoke(String methodName, Object... args) {
        return createResponse(methodName, null, args);
    }

    protected final <T> Response<T> authInvoke(String methodName, Object... args) {
        Session currentSession = sessionManager.getCurrentSession();
        AuthToken token = currentSession == null ? null : currentSession.getToken();
        return createResponse(methodName, token, args);
    }

    private <T> Response<T> createResponse(String methodName, AuthToken token, Object... args) {
        ServiceRequest request = new ServiceRequest(token, serviceName, methodName, args);
        return responder.respond(new Request<T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T process() throws RequestFailedException {
                try (ServiceConnection connection = connectionFactory.createConnection()) {
                    return (T) connection.resolve(request);
                } catch (IOException | ServiceRequestFailedException | ServiceConnectionFailedException e) {
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
