package com.savvato.basemobileapp.services;

import java.util.Optional;

import com.savvato.basemobileapp.config.principal.UserPrincipal;
import com.savvato.basemobileapp.entities.User;
import com.savvato.basemobileapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceVOTING implements UserDetailsService {

	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> opt = userRepo.findByEmail(email);
		User rtn = null;

		if (opt.isPresent())
			rtn = opt.get();
		else
			throw new UsernameNotFoundException(email);

		return new UserPrincipal(rtn);
	}
}
