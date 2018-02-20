package com.kush.lib.service.server.api;

import java.lang.reflect.Method;

public interface ServiceInvokerFactory {

    public static final ServiceInvokerFactory DEFAULT = new DefaultServiceInvokerFactory();

    ServiceInvoker create(BaseService service, Method method);

    public static class DefaultServiceInvokerFactory implements ServiceInvokerFactory {

        @Override
        public ServiceInvoker create(BaseService service, Method method) {
            return new ServiceInvoker(service, method);
        }
    }
}
