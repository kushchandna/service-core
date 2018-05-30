package com.kush.utils.signaling;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.server.Resolver;

public class RemoteSignalsResolver implements Resolver<Signal<?>> {

    private final SignalSpace signalSpace;

    public RemoteSignalsResolver(SignalSpace signalSpace) {
        this.signalSpace = signalSpace;
    }

    @Override
    public Object resolve(Signal<?> signal) throws ResolutionFailedException {
        signalSpace.emit(signal);
        return null;
    }
}
