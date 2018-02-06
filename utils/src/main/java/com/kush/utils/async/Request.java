package com.kush.utils.async;

public interface Request<T> {

    T process() throws RequestFailedException;
}
