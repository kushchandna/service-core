package com.kush.lib.service.client.api;

public interface ServiceInvoker {

    Object invoke(String serviceName, String methodName, Object... arguments) throws RemoteServiceInvocationFailedException;
}
