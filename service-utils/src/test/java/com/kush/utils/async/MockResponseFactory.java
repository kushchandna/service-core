package com.kush.utils.async;

public class MockResponseFactory {

    public static <T> Response<T> aSuccessResponse(T result) {
        LocalResponse<T> response = new LocalResponse<>();
        response.setResult(result);
        return response;
    }

    public static <T> Response<T> aFailingResponse(Exception error) {
        LocalResponse<T> response = new LocalResponse<>();
        response.setError(new RequestFailedException(error));
        return response;
    }

    private MockResponseFactory() {
    }
}
