package com.kush.service.auth.credentials;

import java.util.List;
import java.util.Optional;

import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistenceOperationFailedException;
import com.kush.lib.service.remoting.auth.Credential;
import com.kush.lib.service.remoting.auth.User;
import com.kush.service.auth.credentials.handlers.ComboCredentialHandler;
import com.kush.service.auth.credentials.handlers.DirectLoginCredentialHandler;
import com.kush.service.auth.credentials.handlers.PasswordBasedCredentialHandler;

public class DefaultUserCredentialPersister extends DelegatingPersister<UserCredential>
		implements UserCredentialPersister {

	private final CredentialHandler credentialHandler;

	public DefaultUserCredentialPersister(Persister<UserCredential> delegate) {
		super(delegate);
		credentialHandler = ComboCredentialHandler.combo(
				new DirectLoginCredentialHandler(),
				new PasswordBasedCredentialHandler());
	}

	@Override
	public Optional<User> getUserForCredential(Credential credential) throws PersistenceOperationFailedException {
		List<UserCredential> allUserCredentials = fetchAll();
		for (UserCredential userCredential : allUserCredentials) {
			Credential savedCredential = userCredential.getCredential();
			if (credentialHandler.canHandle(savedCredential)) {
				if (credentialHandler.matches(credential, savedCredential)) {
					return Optional.of(userCredential.getUser());
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public void addUserCredential(User user, Credential credential) throws PersistenceOperationFailedException {
		List<UserCredential> allUserCredentials = fetchAll();
		for (UserCredential userCredential : allUserCredentials) {
			Credential savedCredential = userCredential.getCredential();
			if (credentialHandler.canHandle(savedCredential)) {
				if (credentialHandler.conflicts(credential, savedCredential)) {
					throw new PersistenceOperationFailedException();
				}
			}
		}
		save(new UserCredential(user, credential));
	}
}
