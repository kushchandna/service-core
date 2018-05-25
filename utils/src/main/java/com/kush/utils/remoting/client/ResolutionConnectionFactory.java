package com.kush.utils.remoting.client;

public interface ResolutionConnectionFactory {

    ResolutionConnection createConnection() throws ResolutionConnectionFailedException;
}
