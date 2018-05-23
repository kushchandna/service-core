package com.kush.utils.remoting.client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFailedException;
import com.kush.utils.remoting.server.ResultCode;

public class SocketConnection implements Connection {

    private final Socket socket;

    public SocketConnection(String host, int port) throws ConnectionFailedException {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new ConnectionFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Object resolve(Request<?> request) throws RequestFailedException {
        try {
            sendRequest(request, socket);
            return readResult(socket);
        } catch (ClassNotFoundException | IOException e) {
            throw new RequestFailedException(e);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private static <T> void sendRequest(Request<?> request, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(request);
    }

    private static Object readResult(Socket socket) throws IOException, ClassNotFoundException, RequestFailedException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        int resultCode = ois.readInt();
        Object resultObject = ois.readObject();
        if (resultCode != ResultCode.CODE_SUCCESS) {
            throw (RequestFailedException) resultObject;
        } else {
            return resultObject;
        }
    }
}
