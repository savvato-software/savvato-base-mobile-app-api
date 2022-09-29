package org.haxwell.votingapp.services;

import org.haxwell.votingapp.config.principal.UserPrincipal;

public interface UserPrincipalService {
	public UserPrincipal getUserPrincipalByName(String name);
	public UserPrincipal getUserPrincipalByEmail(String email);
}
