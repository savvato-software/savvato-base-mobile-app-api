package org.haxwell.votingapp.services;

import org.haxwell.votingapp.controllers.dto.UserRequest;
import org.haxwell.votingapp.entities.User;

import java.util.Optional;

public interface UserService {
	public Optional<User> createNewUser(UserRequest req, String preferredContactMethod);

	public Optional<User> find(String query);
	public Optional<User> findById(Long id);
	Optional<User> update(UserRequest request);

	User changeLostPassword(String pw, String phoneNumber, String challengeCode);
}
