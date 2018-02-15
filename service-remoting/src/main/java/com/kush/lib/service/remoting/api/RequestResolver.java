package com.kush.lib.service.remoting.api;

public interface RequestResolver {

    <T> T resolve(ServiceInvocationRequest request, Class<T> returnType);
}
