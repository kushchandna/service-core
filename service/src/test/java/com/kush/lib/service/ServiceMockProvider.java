package com.kush.lib.service;

import java.lang.reflect.Method;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

public class ServiceMockProvider {

    private final ServiceRequestResolver serviceRequestResolver;

    public ServiceMockProvider(TestApplicationServer server) {
        serviceRequestResolver = server.getServiceRequestResolver();
    }

    public <S extends BaseService> S mock(Class<S> serviceClass) {
        if (!serviceClass.isAnnotationPresent(Service.class)) {
            throw new IllegalStateException("Only services can be mocked");
        }
        Service service = serviceClass.getAnnotation(Service.class);
        String serviceName = service.name();
        return Mockito.mock(serviceClass, new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Method method = invocation.getMethod();
                if (!method.isAnnotationPresent(ServiceMethod.class)) {
                    throw new IllegalStateException("Should only call service methods");
                }
                AuthToken authToken = null;
                if (method.isAnnotationPresent(AuthenticationRequired.class)) {
                    authToken = getToken();
                }
                ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
                String serviceMethodName = serviceMethod.name();
                ServiceRequest serviceRequest =
                        new ServiceRequest(authToken, serviceName, serviceMethodName, invocation.getArguments());
                return serviceRequestResolver.resolve(serviceRequest);
            }
        });
    }

    private AuthToken getToken() {
        return null;
    }
}
