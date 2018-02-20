package com.kush.lib.service.remoting.api;

public interface ServiceRequestResolver {

    <T> T resolve(ServiceRequest<T> request) throws ServiceRequestFailedException;

    public static class ReturnType<T> {

        public static <T> ReturnType<T> type() {
            return new ReturnType<T>();
        }

        private ReturnType() {
        }

        @SuppressWarnings("unchecked")
        public T cast(Object data) {
            return (T) data;
        }
    }
}
