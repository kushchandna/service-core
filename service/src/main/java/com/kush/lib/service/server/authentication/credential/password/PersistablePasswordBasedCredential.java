package com.kush.lib.service.server.authentication.credential.password;

import com.kush.lib.persistence.helpers.PersistableObject;
import com.kush.lib.service.remoting.auth.password.PasswordBasedCredential;
import com.kush.utils.id.Identifier;

public class PersistablePasswordBasedCredential extends PersistableObject<PasswordBasedCredential> {

    public PersistablePasswordBasedCredential(PasswordBasedCredential object) {
        super(object);
    }

    public PersistablePasswordBasedCredential(Identifier id, PasswordBasedCredential object) {
        super(id, object);
    }
}
