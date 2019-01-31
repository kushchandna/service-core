package com.kush.utils.beta.remoting.server.http;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.kush.utils.remoting.Resolvable;

public class ResolvableHandler<T extends Resolvable> extends AbstractHandler {

    @Override
    @SuppressWarnings("unchecked")
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ServletInputStream inputStream = request.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        T resolvable = null;
        try {
            resolvable = (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage(), e);
        }
        resolvable.toString();
    }
}
