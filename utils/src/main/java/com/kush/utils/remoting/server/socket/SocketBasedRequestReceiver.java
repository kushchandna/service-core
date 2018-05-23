package com.kush.utils.remoting.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.remoting.server.RequestReceiver;
import com.kush.utils.remoting.server.ResolvableRequest;
import com.kush.utils.remoting.server.ResultCode;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class SocketBasedRequestReceiver extends RequestReceiver {

    private final int port;

    private ServerSocket serverSocket;

    public SocketBasedRequestReceiver(Executor executor, int port) {
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
    protected ResolvableRequest getNextRequest() throws RequestFailedException {
        try {
            Socket socket = serverSocket.accept();
            Request<?> request = readRequest(socket);
            return new ResolvableRequest(request, new SocketBasedResponseListener(socket));
        } catch (IOException | ClassNotFoundException e) {
            throw new RequestFailedException(e);
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

    private static void writeError(Socket socket, RequestFailedException e) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeInt(ResultCode.CODE_ERROR);
        oos.writeObject(e);
    }

    private static Request<?> readRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (Request<?>) ois.readObject();
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
                onError(new RequestFailedException(e));
            } finally {
                closeSocket();
            }
        }

        @Override
        public void onError(RequestFailedException e) {
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
