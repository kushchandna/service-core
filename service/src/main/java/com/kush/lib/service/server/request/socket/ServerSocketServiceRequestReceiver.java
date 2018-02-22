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
import com.kush.lib.service.server.request.ShutdownFailedException;
import com.kush.lib.service.server.request.StartupFailedException;

public class ServerSocketServiceRequestReceiver extends ServiceRequestReceiver<SocketServiceRequestProvider> {

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
    protected SocketServiceRequestProvider getNextRequest() throws ServiceRequestFailedException {
        try {
            Socket socket = serverSocket.accept();
            ServiceRequest<?> request = readRequest(socket);
            return new SocketServiceRequestProvider(request, socket);
        } catch (IOException | ClassNotFoundException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void sendResult(SocketServiceRequestProvider requestProvider, Object result) throws ServiceRequestFailedException {
        try {
            writeResult(requestProvider.getSocket(), result);
        } catch (IOException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void sendError(ServiceRequestFailedException e) {
        // TODO
    }

    @Override
    protected void performStop() throws ShutdownFailedException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ShutdownFailedException(e.getMessage(), e);
        }
    }

    private void writeResult(Socket socket, Object result) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(result);
    }

    private ServiceRequest<?> readRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (ServiceRequest<?>) ois.readObject();
    }
}
