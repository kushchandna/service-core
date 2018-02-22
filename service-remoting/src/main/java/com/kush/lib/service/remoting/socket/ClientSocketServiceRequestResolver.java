package com.kush.lib.service.remoting.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public class ClientSocketServiceRequestResolver implements ServiceRequestResolver {

    private final String host;
    private final int port;

    public ClientSocketServiceRequestResolver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public <T> T resolve(ServiceRequest<T> request) throws ServiceRequestFailedException {
        try (Socket socket = new Socket(host, port)) {
            sendRequest(request, socket);
            Object result = readResult(socket);
            return request.getReturnType().cast(result);
        } catch (IOException | ClassNotFoundException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    private Object readResult(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }

    private <T> void sendRequest(ServiceRequest<T> request, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(request);
    }
}
