package com.kush.utils.signaling.client;

import java.io.IOException;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.ResolutionConnectionFailedException;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalHandler;
import com.kush.utils.signaling.SignalSpace;

public class ClientSignalSpace extends SignalSpace implements Resolver<Signal<?>> {

    private final ClientInfo clientInfo;
    private final ResolutionConnectionFactory registrationRequestConnectionFactory;

    public ClientSignalSpace(SignalEmitter signalEmitter, ClientInfo clientInfo,
            ResolutionConnectionFactory registrationRequestConnectionFactory) {
        super(signalEmitter);
        this.clientInfo = clientInfo;
        this.registrationRequestConnectionFactory = registrationRequestConnectionFactory;
    }

    @Override
    public Object resolve(Signal<?> signal) throws ResolutionFailedException {
        emit(signal);
        return null;
    }

    @Override
    public synchronized <R extends SignalHandler, S extends Signal<R>> void register(Class<S> signalClass, R handler,
            Object filter) {
        performRemoteRegistration(signalClass, filter);
        super.register(signalClass, handler, filter);
    }

    private void performRemoteRegistration(Class<? extends Signal<?>> signalClass, Object filter) {
        SignalHandlerRegistrationRequest registrationRequest =
                new SignalHandlerRegistrationRequest(clientInfo, signalClass, filter);
        try (ResolutionConnection connection = registrationRequestConnectionFactory.createConnection()) {
            connection.resolve(registrationRequest);
        } catch (IOException | ResolutionConnectionFailedException | ResolutionFailedException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
