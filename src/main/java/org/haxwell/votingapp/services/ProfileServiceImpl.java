package org.haxwell.votingapp.services;

import java.util.Optional;

import org.haxwell.votingapp.controllers.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.haxwell.votingapp.dto.ProfileDTO;
import org.haxwell.votingapp.entities.User;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	UserService userService;

	public Optional<ProfileDTO> getByUserId(Long userId) {
		Optional<User> opt = userService.findById(userId);
		ProfileDTO rtn = new ProfileDTO();

		if (opt.isPresent()) {
			User u = opt.get();

			rtn.name = u.getName();
			rtn.email = u.getEmail();
			rtn.phone = u.getPhone();
			rtn.created = u.getCreated().toInstant().toEpochMilli() + "";
			rtn.lastUpdated = u.getLastUpdated().toInstant().toEpochMilli() + "";
		}

		return Optional.of(rtn);
	}

	public boolean update(Long userId, String name, String email, String phone) {
		UserRequest ur = new UserRequest();

		ur.id = userId;
		ur.name = name;
		ur.email = email;
		ur.phone = phone;

		return this.userService.update(ur).isPresent();
	}
}
