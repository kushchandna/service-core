package com.kush.lib.service.sample.local;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.local.LocalApplicationServer;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ErrorListener;
import com.kush.utils.async.Response.ResultListener;
import com.kush.utils.exceptions.ObjectNotFoundException;
import com.kush.utils.id.Identifier;
import com.kush.utils.id.SequentialIdGenerator;

public class SampleLocalApplication {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(SampleLocalApplication.class);

    public static void main(String[] args) throws Exception {
        setupServer();
        ServiceConnectionFactory connFactory = createLocalServerBasedConnectionFactory();
        ApplicationClient client = setupClient(connFactory);
        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeSayHello(serviceClientProvider);
        PasswordBasedCredential credential = new PasswordBasedCredential("testusr", "testpwd".toCharArray());
        registerUser(serviceClientProvider, credential);
        doLogin(serviceClientProvider, credential);
        invokeSayHelloToMe(serviceClientProvider);
        doLogout(serviceClientProvider);
    }

    private static void setupServer() throws StartupFailedException {
        ApplicationServer server = new LocalApplicationServer();
        server.registerService(SampleHelloService.class);
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .withPersistor(UserCredential.class, new InMemoryUserCredentialPersistor())
            .build();
        server.start(context);
    }

    private static ServiceConnectionFactory createLocalServerBasedConnectionFactory() {
        return new LocalServiceConnectionFactory();
    }

    private static ApplicationClient setupClient(ServiceConnectionFactory connFactory) throws Exception {
        ApplicationClient client = new ApplicationClient();
        client.start(connFactory);
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);
        client.activateLoginServiceClient(executor);
        return client;
    }

    private static void invokeSayHello(ServiceClientProvider serviceClientProvider) throws Exception {
        SampleHelloServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleHelloServiceClient.class);
        Response<String> response = sampleServiceClient.sayHello("TestUser");
        LOGGER.info("[APP] sayHello Result received: %s", response.getResult());
    }

    private static void invokeSayHelloToMe(ServiceClientProvider serviceClientProvider) throws Exception {
        SampleHelloServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleHelloServiceClient.class);
        Response<String> response = sampleServiceClient.sayHelloToMe();
        response.addResultListener(new ResultListener<String>() {
            @Override
            public void onResult(String result) {
                LOGGER.info("[APP] sayHelloToMe Result received: %s", result);
            }
        });
        response.addErrorListener(new ErrorListener() {
            @Override
            public void onError(RequestFailedException error) {
                LOGGER.info("[APP] Error occured: %s", error.getMessage());
            }
        });
    }

    private static void doLogin(ServiceClientProvider serviceClientProvider, PasswordBasedCredential credential)
            throws Exception {
        LoginServiceClient loginServiceClient = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        Response<AuthToken> response = loginServiceClient.login(credential);
        response.waitForResult();
    }

    private static void doLogout(ServiceClientProvider serviceClientProvider)
            throws ObjectNotFoundException, InterruptedException, RequestFailedException {
        LoginServiceClient loginServiceClient = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        Response<Void> response = loginServiceClient.logout();
        response.waitForResult();
    }

    private static void registerUser(ServiceClientProvider serviceClientProvider, PasswordBasedCredential credential)
            throws ObjectNotFoundException, InterruptedException, RequestFailedException {
        LoginServiceClient loginServiceClient = serviceClientProvider.getServiceClient(LoginServiceClient.class);
        Response<User> response = loginServiceClient.register(credential);
        response.waitForResult();
    }

    private static final class InMemoryUserCredentialPersistor extends InMemoryPersistor<UserCredential> {

        private InMemoryUserCredentialPersistor() {
            super(new SequentialIdGenerator());
        }

        @Override
        protected UserCredential createPersistableObject(Identifier id, UserCredential reference) {
            return new UserCredential(id, reference.getUser(), reference.getCredential());
        }
    }
}
