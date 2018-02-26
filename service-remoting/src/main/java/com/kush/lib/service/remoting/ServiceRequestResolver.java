package com.kush.lib.service.remoting;

public interface ServiceRequestResolver {

    Object resolve(ServiceRequest request) throws ServiceRequestFailedException;
}
