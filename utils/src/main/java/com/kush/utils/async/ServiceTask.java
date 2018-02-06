package com.kush.utils.async;

public interface ServiceTask<T> {

    T execute() throws ServiceFailedException;
}
