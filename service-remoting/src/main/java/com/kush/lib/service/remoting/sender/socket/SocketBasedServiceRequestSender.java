package com.kush.lib.service.remoting.sender.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.sender.ServiceRequestSender;

public class SocketBasedServiceRequestSender implements ServiceRequestSender {

    private final String host;
    private final int port;

    private Socket socket;

    public SocketBasedServiceRequestSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() throws StartupFailedException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new StartupFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void stop() throws ShutdownFailedException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new ShutdownFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void sendRequest(ServiceRequest<?> request) throws ServiceRequestFailedException {
        try {
            sendRequest(request, socket);
        } catch (IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Object getResult() throws ServiceRequestFailedException {
        try {
            return readResult(socket);
        } catch (ClassNotFoundException | IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    private static Object readResult(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }

    private static <T> void sendRequest(ServiceRequest<T> request, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(request);
    }
}
