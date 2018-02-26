package com.kush.lib.service.remoting.receiver.socket;

import java.net.Socket;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestProvider;

public class SocketServiceRequestProvider extends ServiceRequestProvider {

    private final Socket socket;

    public SocketServiceRequestProvider(ServiceRequest request, Socket socket) {
        super(request);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
