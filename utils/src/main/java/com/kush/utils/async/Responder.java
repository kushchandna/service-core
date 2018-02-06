package com.kush.utils.async;

import java.util.concurrent.Executor;

public class Responder {

    private final Executor executor;

    public Responder(Executor executor) {
        this.executor = executor;
    }

    public <T> Response<T> process(Request<T> request) {
        Response<T> response = new Response<>();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    T result = request.process();
                    sendResult(response, result);
                } catch (RequestFailedException e) {
                    sendError(response, e);
                }
            }
        });
        return response;
    }

    private <T> void sendResult(Response<T> response, T result) {
        response.setResult(result);
    }

    private <T> void sendError(Response<T> response, Exception e) {
        response.setError(e);
    }
}
