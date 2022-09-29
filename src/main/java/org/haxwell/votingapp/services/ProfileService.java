package org.haxwell.votingapp.services;

import org.haxwell.votingapp.dto.ProfileDTO;
import org.haxwell.votingapp.entities.Profile;

import java.util.Optional;

public interface ProfileService {
	Optional<ProfileDTO> getByUserId(Long id);

	boolean update(Long userId, String name, String email, String phone);
}

