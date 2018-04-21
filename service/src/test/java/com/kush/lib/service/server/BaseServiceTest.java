package com.kush.lib.service.server;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.lib.service.server.authentication.SessionManager;
import com.kush.lib.service.server.authentication.UserRegistrationFailedException;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.utils.id.SequentialIdGenerator;

public class BaseServiceTest {

    protected static final Instant CURRENT_TIME = Instant.now();
    protected static final ZoneId CURRENT_ZONE = ZoneId.systemDefault();
    private static final Clock CLOCK = Clock.fixed(CURRENT_TIME, CURRENT_ZONE);

    private final Map<User, Credential> userVsCredential = new HashMap<>();
    private final Context context = new Context();
    private final int numOfUsers;

    private LoginService loginService;

    public BaseServiceTest() {
        this(5);
    }

    public BaseServiceTest(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    @Before
    public final void parentBeforeEachTest() throws Throwable {
        setupBasicMockContext();
        loginService = new LoginService();
        ((BaseService) loginService).initialize(context);
        createUsers();
    }

    @After
    public final void parentAfterEachTest() {
        try {
            endSession();
        } catch (Exception e) {
            // do nothing
        }
        userVsCredential.clear();
        loginService = null;
    }

    protected final void registerService(BaseService service) {
        service.initialize(context);
    }

    protected final User getTestUser() {
        return getUsers()[0];
    }

    protected final User[] getUsers() {
        return userVsCredential.keySet().toArray(new User[userVsCredential.size()]);
    }

    protected final void runAuthenticatedOperation(Operation operation) throws Exception {
        runAuthenticatedOperation(getTestUser(), operation);
    }

    protected final void runAuthenticatedOperation(User user, Operation operation) throws Exception {
        beginSession(user);
        operation.run();
        endSession();
    }

    protected final void addToContext(Object key, Object value) {
        context.addInstance(key, value);
    }

    private void beginSession(User user) throws Exception {
        AuthToken token = loginService.login(userVsCredential.get(user));
        context.getInstance(Auth.class).login(token);
    }

    private void endSession() throws Exception {
        loginService.logout();
        context.getInstance(Auth.class).logout();
    }

    private void createUsers() throws UserRegistrationFailedException {
        for (int i = 1; i <= numOfUsers; i++) {
            Credential credential = new PasswordBasedCredential("testusr" + i, ("testpwd" + i).toCharArray());
            User user = loginService.register(credential);
            userVsCredential.put(user, credential);
        }
    }

    private void setupBasicMockContext() {
        Persistor<UserCredential> userCredPersistor = InMemoryPersistor.forType(UserCredential.class);
        addToContext(Auth.class, new Auth());
        addToContext(SessionManager.class, new SessionManager());
        addToContext(LoginService.KEY_USER_ID_GEN, new SequentialIdGenerator());
        addToContext(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(userCredPersistor));
        addToContext(Clock.class, CLOCK);
    }

    protected static interface Operation {

        void run() throws Exception;
    }
}
