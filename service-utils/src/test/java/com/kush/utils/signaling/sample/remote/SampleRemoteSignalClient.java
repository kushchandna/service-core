package com.kush.utils.signaling.sample.remote;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketBasedResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.client.ClientInfo;
import com.kush.utils.signaling.client.ClientSignalSpace;
import com.kush.utils.signaling.client.socket.SocketClientInfo;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;

public class SampleRemoteSignalClient {

    private static final String CLIENT_HOST = "localhost";

    private static final int PORT_REG_REQ_SENDER = 8888;
    private static final int PORT_SIGNAL_RECEIVER = 8889;

    public static void main(String[] args) {
        ClientInfo clientInfo = new SocketClientInfo(CLIENT_HOST, PORT_SIGNAL_RECEIVER);
        ClientSignalSpace signalSpace = startClient(clientInfo);
        registerForSampleMessageSignal(signalSpace);
    }

    private static ClientSignalSpace startClient(ClientInfo clientInfo) {
        Executor executor = Executors.newFixedThreadPool(2);
        ClientSignalSpace signalSpace = createClientSignalSpace(clientInfo);
        startReceivingRemoteSignals(executor, clientInfo, signalSpace);
        return signalSpace;
    }

    private static ClientSignalSpace createClientSignalSpace(ClientInfo clientInfo) {
        ResolutionConnectionFactory regReqConnFactory =
                new SocketBasedResolutionConnectionFactory(CLIENT_HOST, PORT_REG_REQ_SENDER);
        return new ClientSignalSpace(clientInfo, regReqConnFactory);
    }

    private static void registerForSampleMessageSignal(ClientSignalSpace signalSpace) {
        signalSpace.register(SampleMessageSignal.class, (text) -> {
            System.out.println("Client with User A got message: " + text);
        }, "Remote User A");
    }

    private static void startReceivingRemoteSignals(Executor executor, ClientInfo clientInfo, ClientSignalSpace signalSpace) {
        ResolutionRequestsReceiver signalReceiver = clientInfo.getSignalReceiver(executor);
        signalReceiver.addResolver(Signal.class, signalSpace);
        try {
            signalReceiver.start();
        } catch (StartupFailedException e) {
            e.printStackTrace();
        }
    }
}
