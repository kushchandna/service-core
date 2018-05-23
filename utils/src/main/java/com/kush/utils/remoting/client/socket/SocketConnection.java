package com.kush.utils.remoting.client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.ResultCode;
import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFailedException;

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
    public Object resolve(Resolvable resolvable) throws ResolutionFailedException {
        try {
            sendForResolution(resolvable, socket);
            return readResult(socket);
        } catch (ClassNotFoundException | IOException e) {
            throw new ResolutionFailedException(e);
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private static <T> void sendForResolution(Resolvable resolvable, Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(resolvable);
    }

    private static Object readResult(Socket socket) throws IOException, ClassNotFoundException, ResolutionFailedException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        int resultCode = ois.readInt();
        Object resultObject = ois.readObject();
        if (resultCode != ResultCode.CODE_SUCCESS) {
            throw (ResolutionFailedException) resultObject;
        } else {
            return resultObject;
        }
    }
}
