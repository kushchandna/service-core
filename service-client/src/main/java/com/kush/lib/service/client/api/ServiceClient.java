package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Responder;
import com.kush.utils.async.Response;

public abstract class ServiceClient {

    private final String serviceName;

    private Responder responder;
    private ServiceRequestResolver requestResolver;

    public ServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    public void activate(Executor executor, ServiceRequestResolver requestResolver) {
        this.requestResolver = requestResolver;
        responder = new Responder(executor);
    }

    protected final <T> Response<T> invoke(String methodName, Object... args) {
        return responder.respond(new Request<T>() {

            @Override
            @SuppressWarnings("unchecked")
            public T process() throws RequestFailedException {
                try {
                    ServiceRequest request = new ServiceRequest(null, serviceName, methodName, args);
                    Object result = requestResolver.resolve(request);
                    return (T) result;
                } catch (ServiceRequestFailedException e) {
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
