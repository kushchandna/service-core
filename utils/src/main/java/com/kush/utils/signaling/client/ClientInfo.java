package com.kush.utils.signaling.client;

import java.io.Serializable;
import java.util.concurrent.Executor;

import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;

public interface ClientInfo extends Serializable {

    ResolutionConnectionFactory getSignalSenderConnectionFactory();

    ResolutionRequestsReceiver getSignalReceiver(Executor executor);
}
