package com.kush.lib.service.client.api;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
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

    public ServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    public void activate(Executor executor, ServiceConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        responder = new Responder(executor);
    }

    protected final <T> Response<T> invoke(String methodName, Object... args) {
        return responder.respond(new Request<T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T process() throws RequestFailedException {
                ServiceRequest request = new ServiceRequest(null, serviceName, methodName, args);
                try (ServiceConnection connection = connectionFactory.createConnection()) {
                    return (T) connection.resolve(request);
                } catch (IOException | ServiceRequestFailedException | ServiceConnectionFailedException e) {
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
