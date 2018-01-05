package com.kush.lib.service.client.api;

import java.util.concurrent.CountDownLatch;

public class Response<T> {

    private final CountDownLatch latch;

    private T result;
    private Exception error;
    private ResultListener<T> resultListener;

    public Response() {
        latch = new CountDownLatch(1);
    }

    public T getResult() throws ServiceFailedException, InterruptedException {
        latch.await();
        if (error != null) {
            throw new ServiceFailedException(error);
        }
        return result;
    }

    public void setResultListener(ResultListener<T> resultListener) {
        this.resultListener = resultListener;
        if (result != null) {
            resultListener.onResult(result);
        }
    }

    void setResult(T result) {
        if (latch.getCount() == 0) {
            return;
        }
        this.result = result;
        latch.countDown();
        if (resultListener != null) {
            resultListener.onResult(result);
        }
    }

    void setError(Exception error) {
        if (latch.getCount() == 0) {
            return;
        }
        this.error = error;
        latch.countDown();
    }

    public static interface ResultListener<R> {

        void onResult(R result);
    }

    public static interface ErrorListener<E extends Exception> {

        void onError(E error);
    }
}
