package com.kush.lib.service.server.request.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.server.request.ServiceRequestReceiver;
import com.kush.lib.service.server.request.StartupFailedException;

public class ServerSocketServiceRequestReceiver extends ServiceRequestReceiver {

    private final int port;

    private ServerSocket serverSocket;

    public ServerSocketServiceRequestReceiver(ServiceRequestResolver requestResolver, Executor executor, int port) {
        super(requestResolver, executor);
        this.port = port;
    }

    @Override
    protected void performStartup() throws StartupFailedException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new StartupFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected ServiceRequest<?> getNextRequest() throws ServiceRequestFailedException {
        try {
            Socket socket = serverSocket.accept();
            return readRequest(socket);
        } catch (IOException | ClassNotFoundException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void sendResult(ServiceRequest<?> request, Object result) {
    }

    @Override
    protected void sendError(ServiceRequestFailedException e) {
        // TODO
    }

    @Override
    protected void performStop() {
        // TODO Auto-generated method stub

    }

    void writeResult(OutputStream os, Object result) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(result);
    }

    private ServiceRequest<?> readRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (ServiceRequest<?>) ois.readObject();
    }
}
