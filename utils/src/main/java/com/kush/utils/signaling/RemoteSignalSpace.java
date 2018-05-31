package com.kush.utils.signaling;

import java.util.concurrent.Executor;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;

public class RemoteSignalSpace extends SignalSpace implements Resolver<SignalHandlerRegistrationRequest> {

    public RemoteSignalSpace(Executor executor, SignalEmitter signalEmitter) {
        super(executor, signalEmitter);
    }

    @Override
    public Object resolve(SignalHandlerRegistrationRequest registrationRequest) throws ResolutionFailedException {
        ResolutionConnectionFactory connectionFactory = registrationRequest.getClientInfo().getSignalSenderConnectionFactory();
        SignalHandler handler = new RemoteSignalSender(connectionFactory);
        registerWithoutCheckingType(registrationRequest.getSignalType(), handler, registrationRequest.getFilter());
        return null;
    }
}
