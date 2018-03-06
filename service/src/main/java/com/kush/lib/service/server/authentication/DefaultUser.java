package com.kush.lib.service.server.authentication;

import com.kush.lib.service.remoting.auth.User;
import com.kush.utils.id.Identifier;

public class DefaultUser implements User {

    private static final long serialVersionUID = 1L;

    private final Identifier userId;

    public DefaultUser(Identifier userId) {
        this.userId = userId;
    }

    @Override
    public Identifier getId() {
        return userId;
    }
}
