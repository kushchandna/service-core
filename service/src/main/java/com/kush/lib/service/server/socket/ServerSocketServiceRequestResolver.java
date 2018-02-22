package com.kush.lib.service.server.socket;

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

public class ServerSocketServiceRequestResolver implements Runnable {

    private final ServiceRequestResolver underlyingResolver;
    private final int port;
    private final Executor executor;

    private ServerSocket serverSocket;
    private volatile boolean running = false;

    public ServerSocketServiceRequestResolver(ServiceRequestResolver underlyingResolver, int port, Executor executor) {
        this.underlyingResolver = underlyingResolver;
        this.port = port;
        this.executor = executor;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ServiceRequest<?> request = readRequest(socket);
                writeResult(socket, request);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeResult(Socket socket, ServiceRequest<?> request) throws IOException {
        executor.execute(new Task(request, socket.getOutputStream()));
    }

    private void writeResult(OutputStream os, Object result) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(result);
    }

    private ServiceRequest<?> readRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        ServiceRequest<?> request = (ServiceRequest<?>) ois.readObject();
        return request;
    }

    public void stop() {
        running = false;
    }

    private final class Task implements Runnable {

        private final ServiceRequest<?> request;
        private final OutputStream os;

        public Task(ServiceRequest<?> request, OutputStream os) {
            this.request = request;
            this.os = os;
        }

        @Override
        public void run() {
            Object result;
            try {
                result = underlyingResolver.resolve(request);
                writeResult(os, result);
            } catch (ServiceRequestFailedException | IOException e) {
            }
        }
    }
}
