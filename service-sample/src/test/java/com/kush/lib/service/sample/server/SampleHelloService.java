package com.kush.lib.service.sample.server;

import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service(name = "Sample Hello Service")
public class SampleHelloService extends BaseService {

    @ServiceMethod(name = "Say Hello")
    public String sayHello(String name) {
        String text = sayHelloInternal(name);
        System.out.println("[Server] returning result for sayHello: " + text);
        return text;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "Say Hello To Me")
    public String sayHelloToMe() {
        User currentUser = getCurrentUser();
        String name = fetchName(currentUser);
        String text = sayHelloInternal(name);
        System.out.println("[Server] returning result for sayHelloToMe: " + text);
        return text;
    }

    private String sayHelloInternal(String name) {
        SampleHelloTextProvider helloTextProvider = getContext().getInstance(SampleHelloTextProvider.class);
        return helloTextProvider.getHelloText() + " " + name;
    }

    private String fetchName(User user) {
        return null;
    }
}
