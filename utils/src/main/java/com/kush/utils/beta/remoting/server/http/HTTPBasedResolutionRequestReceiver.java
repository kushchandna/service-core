package com.kush.utils.beta.remoting.server.http;

import java.util.concurrent.Executor;

import org.eclipse.jetty.server.Server;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.server.ResolutionRequest;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class HTTPBasedResolutionRequestReceiver extends ResolutionRequestsReceiver {

    private final int port;

    private Server server;

    public HTTPBasedResolutionRequestReceiver(Executor requestResolverExecutor, int port) {
        super(requestResolverExecutor);
        this.port = port;
    }

    @Override
    protected void performStartup() throws StartupFailedException {
        server = new Server(port);
        try {
            server.start();
        } catch (Exception e) {
            throw new StartupFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected void performStop() throws ShutdownFailedException {
        try {
            server.stop();
        } catch (Exception e) {
            throw new ShutdownFailedException(e.getMessage(), e);
        }
    }

    @Override
    protected ResolutionRequest getNextResolvableQuery() throws ResolutionFailedException {
        return null;
    }
}
