package com.kush.lib.service.server.request.socket;

import java.net.Socket;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.server.request.ServiceRequestProvider;

public class SocketServiceRequestProvider extends ServiceRequestProvider {

    private final Socket socket;

    public SocketServiceRequestProvider(ServiceRequest<?> request, Socket socket) {
        super(request);
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
