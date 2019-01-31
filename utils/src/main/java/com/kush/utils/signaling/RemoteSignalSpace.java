package com.kush.utils.signaling;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;

public class RemoteSignalSpace extends SignalSpace implements Resolver<SignalHandlerRegistrationRequest> {

    public RemoteSignalSpace(SignalEmitter signalEmitter) {
        super(signalEmitter);
    }

    @Override
    public Object resolve(SignalHandlerRegistrationRequest registrationRequest) throws ResolutionFailedException {
        ResolutionConnectionFactory connectionFactory = registrationRequest.getClientInfo().getSignalSenderConnectionFactory();
        SignalHandler handler = new RemoteSignalSender(connectionFactory);
        registerWithoutCheckingType(registrationRequest.getSignalType(), handler, registrationRequest.getFilter());
        return null;
    }
}
