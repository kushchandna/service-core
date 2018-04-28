package com.kush.lib.service.remoting.receiver.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ResultCode;
import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ResolvableServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;

public class ServerSocketServiceRequestReceiver extends ServiceRequestReceiver {

    private final int port;

    private ServerSocket serverSocket;

    public ServerSocketServiceRequestReceiver(Executor executor, int port) {
        super(executor);
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
    protected ResolvableServiceRequest getNextRequest() throws ServiceRequestFailedException {
        try {
            Socket socket = serverSocket.accept();
            ServiceRequest request = readRequest(socket);
            return new ResolvableServiceRequest(request, new SocketBasedResponseListener(socket));
        } catch (IOException | ClassNotFoundException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void performStop() throws ShutdownFailedException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new ShutdownFailedException(e.getMessage(), e);
        }
    }

    private static void writeResult(Socket socket, Object result) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeInt(ResultCode.CODE_SUCCESS);
        oos.writeObject(result);
    }

    private static void writeError(Socket socket, ServiceRequestFailedException e) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeInt(ResultCode.CODE_ERROR);
        oos.writeObject(e);
    }

    private static ServiceRequest readRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (ServiceRequest) ois.readObject();
    }

    private final class SocketBasedResponseListener implements ResponseListener {

        private final Socket socket;

        public SocketBasedResponseListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void onResult(Object result) {
            try {
                writeResult(socket, result);
            } catch (Exception e) {
                onError(new ServiceRequestFailedException(e.getMessage()));
            } finally {
                closeSocket();
            }
        }

        @Override
        public void onError(ServiceRequestFailedException e) {
            try {
                writeError(socket, e);
            } catch (IOException exception) {
                // could not send error
            } finally {
                closeSocket();
            }
        }

        private void closeSocket() {
            // TODO Auto-generated method stub

        }
    }
}
