package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.lib.service.remoting.api.ServiceRequestResolver.ReturnType;
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
                    ServiceRequest request = new ServiceRequest(serviceName, methodName, args);
                    return requestResolver.resolve(request, ReturnType.type());
                } catch (ServiceRequestFailedException e) {
                    throw new RequestFailedException(e);
                }
            }
        });
    }
}