package com.savvato.basemobileapp.controllers;

import com.savvato.basemobileapp.controllers.dto.ProfileRequest;
import com.savvato.basemobileapp.dto.GenericResponseDTO;
import com.savvato.basemobileapp.dto.ProfileDTO;
import com.savvato.basemobileapp.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ProfileAPIController {

	@Autowired
    ProfileService profileService;
	
	ProfileAPIController() {
			
	}
	
	@RequestMapping(value = { "/api/profile/{profileId}" }, method=RequestMethod.GET)
	public ResponseEntity<ProfileDTO> getById(@PathVariable Long profileId) {
		
		Optional<ProfileDTO> opt = profileService.getByUserId(profileId);

		if (opt.isPresent())
			return ResponseEntity.status(HttpStatus.OK).body(opt.get());
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@RequestMapping(value = { "/api/profile/{profileId}" }, method=RequestMethod.PUT)
	public ResponseEntity<GenericResponseDTO> update(@RequestBody @Valid ProfileRequest request) {
		Boolean isUpdated = profileService.update(request.userId, request.name, request.email, request.phone);
		GenericResponseDTO genericResponseDTO = GenericResponseDTO
				.builder()
				.responseBoolean(isUpdated)
				.build();
		
		if (genericResponseDTO.responseBoolean) {
			return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponseDTO);
		}
	}
}

