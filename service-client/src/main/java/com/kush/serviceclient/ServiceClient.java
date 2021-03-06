package com.kush.serviceclient;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.commons.async.Request;
import com.kush.commons.async.RequestFailedException;
import com.kush.commons.async.Responder;
import com.kush.commons.async.Response;
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.serviceclient.auth.Session;
import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.ResolutionConnectionFailedException;

public abstract class ServiceClient {

    private static final Logger LOGGER = LogManager.getFormatterLogger(ServiceClient.class);

    private final String serviceName;

    private Responder responder;
    private ResolutionConnectionFactory connectionFactory;
    private SessionManager sessionManager;

    public ServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    void activate(Executor executor, ResolutionConnectionFactory connectionFactory, SessionManager sessionManager) {
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
        LOGGER.debug("Creating request '%s'", request);
        LOGGER.info("Sending '%s' request to '%s'", methodName, serviceName);
        return responder.respond(new Request<T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T process() throws RequestFailedException {
                LOGGER.debug("Creating connection for request '%s'", request);
                try (ResolutionConnection connection = connectionFactory.createConnection()) {
                    LOGGER.debug("Resolving '%s' request from '%s'", methodName, serviceName);
                    T result = (T) connection.resolve(request);
                    LOGGER.info("Resolved '%s' request from '%s'", methodName, serviceName);
                    return result;
                } catch (IOException | ResolutionConnectionFailedException | ResolutionFailedException e) {
                    LOGGER.error(e);
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
