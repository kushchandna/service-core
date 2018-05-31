package com.kush.utils.signaling.client;

import com.kush.utils.remoting.Resolvable;
import com.kush.utils.signaling.Signal;

public class SignalHandlerRegistrationRequest implements Resolvable {

    private static final long serialVersionUID = 1L;

    private final ClientInfo clientInfo;
    private final Class<? extends Signal<?>> signalType;
    private final Object filter;

    public SignalHandlerRegistrationRequest(ClientInfo clientInfo, Class<? extends Signal<?>> signalType) {
        this(clientInfo, signalType, Signal.DEFAULT_FILTER);
    }

    public SignalHandlerRegistrationRequest(ClientInfo clientInfo, Class<? extends Signal<?>> signalType, Object filter) {
        this.clientInfo = clientInfo;
        this.signalType = signalType;
        this.filter = filter;
    }

    public Class<? extends Signal<?>> getSignalType() {
        return signalType;
    }

    public Object getFilter() {
        return filter;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}
