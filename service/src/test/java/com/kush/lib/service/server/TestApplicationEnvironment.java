package com.kush.lib.service.server;

import java.util.HashMap;
import java.util.Map;

import org.junit.rules.ExternalResource;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.lib.service.server.authentication.SessionManager;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.utils.id.SequentialIdGenerator;

public class TestApplicationEnvironment extends ExternalResource {

    private final Map<User, Credential> userVsCredential = new HashMap<>();

    private final int numOfUsers;

    private Context context;
    private LoginService loginService;

    public TestApplicationEnvironment() {
        this(5);
    }

    public TestApplicationEnvironment(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    @Override
    protected void before() throws Throwable {
        Persistor<UserCredential> userCredPersistor = InMemoryPersistor.forType(UserCredential.class);
        context = createContextBuilder()
            .withInstance(Auth.class, new Auth())
            .withInstance(SessionManager.class, new SessionManager())
            .withInstance(LoginService.KEY_USER_ID_GEN, new SequentialIdGenerator())
            .withInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(userCredPersistor))
            .build();
        loginService = new LoginService();
        ((BaseService) loginService).initialize(context);
        for (int i = 1; i <= numOfUsers; i++) {
            Credential credential = new PasswordBasedCredential("testusr" + i, ("testpwd" + i).toCharArray());
            User user = loginService.register(credential);
            userVsCredential.put(user, credential);
        }
    }

    @Override
    protected void after() {
        try {
            endSession();
        } catch (Exception e) {
            // eat exception
        }
        userVsCredential.clear();
        loginService = null;
    }

    protected ContextBuilder createContextBuilder() {
        return ContextBuilder.create();
    }

    public void registerService(BaseService service) {
        service.initialize(context);
    }

    public User getTestUser() {
        return getUsers()[0];
    }

    public User[] getUsers() {
        return userVsCredential.keySet().toArray(new User[userVsCredential.size()]);
    }

    public void runAuthenticatedOperation(Operation operation) throws Exception {
        runAuthenticatedOperation(getTestUser(), operation);
    }

    public void runAuthenticatedOperation(User user, Operation operation) throws Exception {
        beginSession(user);
        operation.run();
        endSession();
    }

    private void beginSession(User user) throws Exception {
        AuthToken token = loginService.login(userVsCredential.get(user));
        context.getInstance(Auth.class).login(token);
    }

    private void endSession() throws Exception {
        loginService.logout();
        context.getInstance(Auth.class).logout();
    }

    public static interface Operation {

        void run() throws Exception;
    }
}
