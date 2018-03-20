package com.kush.utils.async;

public interface Response<T> {

    T getResult() throws RequestFailedException, InterruptedException;

    void waitForResult() throws InterruptedException, RequestFailedException;

    void addResultListener(ResultListener<T> resultListener);

    void addErrorListener(ErrorListener errorListener);

    public static interface ResultListener<R> {

        void onResult(R result);
    }

    public static interface ErrorListener {

        void onError(RequestFailedException error);
    }
}
