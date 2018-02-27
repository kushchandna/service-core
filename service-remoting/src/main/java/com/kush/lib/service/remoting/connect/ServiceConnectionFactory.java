package com.kush.lib.service.remoting.connect;

public interface ServiceConnectionFactory {

    ServiceConnection createConnection() throws ServiceConnectionFailedException;
}
