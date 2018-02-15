package com.kush.lib.service.remoting.api;

public interface ServiceRequestResolver {

    <T> T resolve(ServiceInvocationRequest request, Class<T> returnType) throws ServiceRequestFailedException;
}
