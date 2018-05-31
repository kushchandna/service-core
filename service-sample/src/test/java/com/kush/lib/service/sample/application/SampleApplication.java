package com.kush.lib.service.sample.application;

import static com.kush.lib.persistence.helpers.InMemoryPersistor.forType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.service.ApplicationServer;
import com.kush.service.Context;
import com.kush.service.ContextBuilder;
import com.kush.service.auth.credentials.DefaultUserCredentialPersistor;
import com.kush.service.auth.credentials.UserCredential;
import com.kush.service.auth.credentials.UserCredentialPersistor;
import com.kush.serviceclient.ApplicationClient;
import com.kush.serviceclient.ServiceClientActivationFailedException;
import com.kush.serviceclient.ServiceClientProvider;
import com.kush.serviceclient.auth.LoginServiceClient;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ErrorListener;
import com.kush.utils.async.Response.ResultListener;
import com.kush.utils.exceptions.ObjectNotFoundException;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;

public abstract class SampleApplication {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(SampleApplication.class);

    public void setupServer() throws StartupFailedException {
        Executor executor = Executors.newFixedThreadPool(5);
        ResolutionRequestsReceiver serviceRequestReceiver = createResolutionRequestsReceiver(executor);
        ApplicationServer server = new ApplicationServer(serviceRequestReceiver);
        server.registerService(SampleHelloService.class);
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Persistor<UserCredential> delegate = forType(UserCredential.class);
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .withInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(delegate))
            .build();
        server.start(context);
    }

    public ApplicationClient setupClient() throws ServiceClientActivationFailedException {
        ResolutionConnectionFactory connectionFactory = createServiceConnectionFactory();
        ApplicationClient client = new ApplicationClient();
        client.start(connectionFactory);
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);
        client.activateLoginServiceClient(executor);
        return client;
    }

    public void performClientOperations(ApplicationClient client) throws Exception {
        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeSayHello(serviceClientProvider);
        PasswordBasedCredential credential = new PasswordBasedCredential("testusr", "testpwd".toCharArray());
        registerUser(serviceClientProvider, credential);
        doLogin(serviceClientProvider, credential);
        invokeSayHelloToMe(serviceClientProvider);
        doLogout(serviceClientProvider);
    }

    protected abstract ResolutionConnectionFactory createServiceConnectionFactory();

    protected abstract ResolutionRequestsReceiver createResolutionRequestsReceiver(Executor executor);

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
}
