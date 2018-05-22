package com.kush.utils.async.remoting.connection;

import java.io.Closeable;

import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;

public interface Connection extends Closeable {

    Object resolve(Request<?> request) throws RequestFailedException;
}
