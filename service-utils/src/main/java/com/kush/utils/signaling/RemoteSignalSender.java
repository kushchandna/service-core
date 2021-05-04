package com.kush.utils.signaling;

import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;

class RemoteSignalSender implements SignalHandler {

    private final ResolutionConnectionFactory connectionFactory;

    RemoteSignalSender(ResolutionConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    void send(Signal<?> signal) {
        try (ResolutionConnection connection = connectionFactory.createConnection()) {
            connection.resolve(signal);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
