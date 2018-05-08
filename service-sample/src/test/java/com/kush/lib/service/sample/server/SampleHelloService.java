package com.kush.lib.service.sample.server;

import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service
public class SampleHelloService extends BaseService {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(SampleHelloService.class);

    @ServiceMethod
    public String sayHello(String name) {
        String text = sayHelloInternal(name);
        LOGGER.info("[Server] returning result for sayHello: %s", text);
        return text;
    }

    @AuthenticationRequired
    @ServiceMethod
    public String sayHelloToMe() {
        User currentUser = getCurrentUser();
        String name = fetchName(currentUser);
        String text = sayHelloInternal(name);
        LOGGER.info("[Server] returning result for sayHelloToMe: %s", text);
        return text;
    }

    private String sayHelloInternal(String name) {
        SampleHelloTextProvider helloTextProvider = getContext().getInstance(SampleHelloTextProvider.class);
        return helloTextProvider.getHelloText() + " " + name;
    }

    private String fetchName(User user) {
        return user.getId().toString();
    }
}
