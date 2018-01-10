package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

class Responder {

    private final Executor executor;

    public Responder(Executor executor) {
        this.executor = executor;
    }

    public <T> Response<T> invoke(ServiceTask<T> task) {
        Response<T> response = new Response<>();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    T result = task.execute();
                    sendResult(response, result);
                } catch (Exception e) {
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
