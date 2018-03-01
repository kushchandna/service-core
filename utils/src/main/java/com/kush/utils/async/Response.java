package com.kush.utils.async;

import java.util.concurrent.CountDownLatch;

public class Response<T> {

    private final CountDownLatch latch;

    private T result;
    private RequestFailedException error;
    private ResultListener<T> resultListener;
    private ErrorListener errorListener;

    public Response() {
        latch = new CountDownLatch(1);
    }

    public void waitForResult() throws InterruptedException, RequestFailedException {
        latch.await();
        if (error != null) {
            throw new RequestFailedException(error);
        }
    }

    public T getResult() throws RequestFailedException, InterruptedException {
        waitForResult();
        return result;
    }

    public void setResultListener(ResultListener<T> resultListener) {
        this.resultListener = resultListener;
        if (result != null) {
            resultListener.onResult(result);
        }
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
        if (error != null) {
            errorListener.onError(error);
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

    void setError(RequestFailedException error) {
        if (latch.getCount() == 0) {
            return;
        }
        this.error = error;
        latch.countDown();
        if (errorListener != null) {
            errorListener.onError(error);
        }
    }

    public static interface ResultListener<R> {

        void onResult(R result);
    }

    public static interface ErrorListener {

        void onError(RequestFailedException error);
    }
}
