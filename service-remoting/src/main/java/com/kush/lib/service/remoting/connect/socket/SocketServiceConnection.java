package com.kush.lib.service.remoting.connect.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.connect.ServiceConnection;

public class SocketServiceConnection implements ServiceConnection {

    private final Socket socket;

    public SocketServiceConnection(String host, int port) throws ServiceRequestFailedException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Object resolveRequest(ServiceRequest request) throws ServiceRequestFailedException {
        try {
            sendRequest(request, socket);
            return readResult(socket);
        } catch (ClassNotFoundException | IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    private static <T> void sendRequest(ServiceRequest request, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(request);
    }

    private static Object readResult(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }
}
