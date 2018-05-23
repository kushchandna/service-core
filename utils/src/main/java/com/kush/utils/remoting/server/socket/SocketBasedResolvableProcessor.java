package com.kush.utils.remoting.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.ResultCode;
import com.kush.utils.remoting.server.ResolvableProcessor;
import com.kush.utils.remoting.server.ResolvableQuery;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class SocketBasedResolvableProcessor extends ResolvableProcessor {

    private final int port;

    private ServerSocket serverSocket;

    public SocketBasedResolvableProcessor(Executor executor, int port) {
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
    protected ResolvableQuery getNextResolvableQuery() throws ResolutionFailedException {
        try {
            Socket socket = serverSocket.accept();
            Resolvable resolvable = readResolvable(socket);
            return new ResolvableQuery(resolvable, new SocketBasedResponseListener(socket));
        } catch (IOException | ClassNotFoundException e) {
            throw new ResolutionFailedException(e);
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

    private static void writeError(Socket socket, ResolutionFailedException e) throws IOException {
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeInt(ResultCode.CODE_ERROR);
        oos.writeObject(e);
    }

    private static Resolvable readResolvable(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        return (Resolvable) ois.readObject();
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
                onError(new ResolutionFailedException(e));
            } finally {
                closeSocket();
            }
        }

        @Override
        public void onError(ResolutionFailedException e) {
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
