package com.kush.serviceclient;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.serviceclient.auth.Session;
import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Responder;
import com.kush.utils.async.Response;
import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.client.ConnectionFailedException;

public abstract class ServiceClient {

    private static final com.kush.logger.Logger LOGGER = com.kush.logger.LoggerFactory.INSTANCE.getLogger(ServiceClient.class);

    private final String serviceName;

    private Responder responder;
    private ConnectionFactory connectionFactory;
    private SessionManager sessionManager;

    public ServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    void activate(Executor executor, ConnectionFactory connectionFactory, SessionManager sessionManager) {
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating request '%s'", request);
        }
        LOGGER.info("Sending '%s' request to '%s'", methodName, serviceName);
        return responder.respond(new Request<T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T process() throws RequestFailedException {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Creating connection for request '%s'", request);
                }
                try (Connection connection = connectionFactory.createConnection()) {
                    LOGGER.info("Resolving '%s' request from '%s'", methodName, serviceName);
                    T result = (T) connection.resolve(request);
                    LOGGER.info("Resolved '%s' request from '%s'", methodName, serviceName);
                    return result;
                } catch (IOException | ConnectionFailedException | ResolutionFailedException e) {
                    LOGGER.error(e);
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
