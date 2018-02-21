package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public interface ServiceRequestResolverFactory {

    public static final ServiceRequestResolverFactory DEFAULT = new ServiceRequestResolverFactory() {

        @Override
        public ServiceRequestResolver create(ServiceInvokerProvider serviceInvokerProvider) {
            return new DefaultServiceRequestResolver(serviceInvokerProvider);
        }
    };

    ServiceRequestResolver create(ServiceInvokerProvider serviceInvokerProvider);
}
