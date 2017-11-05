package com.kush.lib.service.api.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class Responder<T> {

    private final Executor executor;

    public Responder(Executor executor) {
        this.executor = executor;
    }

    public Response<T> invoke(Callable<T> callable) {
        Response<T> response = new Response<>();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    T result = callable.call();
                    sendResult(response, result);
                } catch (Exception e) {
                    sendError(response, e);
                }
            }
        });
        return response;
    }

    private void sendResult(Response<T> response, T result) {
        response.setResult(result);
    }

    private void sendError(Response<T> response, Exception e) {
        response.setError(e);
    }
}
