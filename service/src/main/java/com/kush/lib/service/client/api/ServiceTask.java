package com.kush.lib.service.client.api;

public interface ServiceTask<T> {

    T execute() throws ServiceFailedException;
}
