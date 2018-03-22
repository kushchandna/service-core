package com.kush.lib.service.remoting.connect.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.lib.service.remoting.ResultCode;
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFailedException;

public class SocketServiceConnection implements ServiceConnection {

    private final Socket socket;

    public SocketServiceConnection(String host, int port) throws ServiceConnectionFailedException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ServiceConnectionFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Object resolve(ServiceRequest request) throws ServiceRequestFailedException {
        try {
            sendRequest(request, socket);
            return readResult(socket);
        } catch (ClassNotFoundException | IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private static <T> void sendRequest(ServiceRequest request, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(request);
    }

    private static Object readResult(Socket socket) throws IOException, ClassNotFoundException, ServiceRequestFailedException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        int resultCode = ois.readInt();
        Object resultObject = ois.readObject();
        if (resultCode != ResultCode.CODE_SUCCESS) {
            throw (ServiceRequestFailedException) resultObject;
        } else {
            return resultObject;
        }
    }
}
