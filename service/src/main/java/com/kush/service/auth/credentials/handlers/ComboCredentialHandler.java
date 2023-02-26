package com.kush.service.auth.credentials.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import com.kush.lib.service.remoting.auth.Credential;
import com.kush.service.auth.credentials.CredentialHandler;

public class ComboCredentialHandler implements CredentialHandler {

	private final Collection<CredentialHandler> handlers;

	private ComboCredentialHandler(Collection<CredentialHandler> handlers) {
		this.handlers = handlers;
	}

	public static CredentialHandler combo(CredentialHandler... handlers) {
		return new ComboCredentialHandler(Arrays.asList(handlers));
	}

	@Override
	public boolean canHandle(Credential credential) {
		return getHandler(credential).isPresent();
	}

	@Override
	public boolean conflicts(Credential credentialToCheck, Credential referenceCredential) {
		Optional<CredentialHandler> handler = getHandler(credentialToCheck);
		if (handler.isEmpty())
			throw new IllegalArgumentException();
		return handler.get().conflicts(credentialToCheck, referenceCredential);
	}

	@Override
	public boolean matches(Credential credentialToCheck, Credential referenceCredential) {
		Optional<CredentialHandler> handler = getHandler(credentialToCheck);
		if (handler.isEmpty())
			throw new IllegalArgumentException();
		return handler.get().matches(credentialToCheck, referenceCredential);
	}

	private Optional<CredentialHandler> getHandler(Credential credentialToCheck) {
		return handlers.stream().filter(h -> h.canHandle(credentialToCheck)).findFirst();
	}
}
