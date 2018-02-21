package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.ServiceRequestResolver.ReturnType;
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
            public T process() throws RequestFailedException {
                try {
                    ServiceRequest<T> request = new ServiceRequest<>(null, serviceName, methodName, ReturnType.type(), args);
                    return requestResolver.resolve(request);
                } catch (ServiceRequestFailedException e) {
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}
