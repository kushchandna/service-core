package com.kush.service.auth.credentials;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;

public class UserCredential implements Identifiable {

    private final Identifier id;
    private final User user;
    private final Credential credential;

    public UserCredential(User user, Credential credential) {
        this(Identifier.NULL, user, credential);
    }

    public UserCredential(Identifier id, UserCredential userCredential) {
        this(id, userCredential.getUser(), userCredential.getCredential());
    }

    public UserCredential(Identifier id, User user, Credential credential) {
        this.id = id;
        this.user = user;
        this.credential = credential;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Credential getCredential() {
        return credential;
    }
}
