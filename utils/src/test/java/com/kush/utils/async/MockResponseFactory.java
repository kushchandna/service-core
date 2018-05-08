package com.kush.utils.async;

public class MockResponseFactory {

    public static <T> Response<T> aSuccessResponse(T result) {
        LocalResponse<T> response = new LocalResponse<>();
        response.setResult(result);
        return response;
    }

    private MockResponseFactory() {
    }
}
