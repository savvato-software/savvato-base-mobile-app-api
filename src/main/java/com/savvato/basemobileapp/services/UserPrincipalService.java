package com.savvato.basemobileapp.services;

import com.savvato.basemobileapp.config.principal.UserPrincipal;

public interface UserPrincipalService {
	public UserPrincipal getUserPrincipalByName(String name);
	public UserPrincipal getUserPrincipalByEmail(String email);
}
