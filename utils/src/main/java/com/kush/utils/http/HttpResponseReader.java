package com.kush.utils.http;

import java.io.InputStream;

public interface HttpResponseReader<T> {

    T read(InputStream inputStream);
}
