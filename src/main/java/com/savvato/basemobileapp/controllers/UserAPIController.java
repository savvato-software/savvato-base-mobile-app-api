package com.savvato.basemobileapp.controllers;

import javax.validation.Valid;

import com.savvato.basemobileapp.constants.Constants;
import com.savvato.basemobileapp.controllers.dto.LostPasswordRequest;
import com.savvato.basemobileapp.controllers.dto.UserRequest;
import com.savvato.basemobileapp.dto.GenericResponseDTO;
import com.savvato.basemobileapp.entities.User;
import com.savvato.basemobileapp.repositories.UserRepository;
import com.savvato.basemobileapp.services.ProfileService;
import com.savvato.basemobileapp.services.SMSChallengeCodeService;
import com.savvato.basemobileapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserAPIController {

	private enum UserData {USER_NAME, USER_EMAIL, USER_PHONE};

	@Autowired
	UserService userService;

	@Autowired
	ProfileService profileService;

	@Autowired
	UserRepository ur;

	@Autowired
	SMSChallengeCodeService smsccs;
	
	UserAPIController() {
		
	}
	
	@RequestMapping(value = { "/api/public/user/new" }, method=RequestMethod.POST)
	public ResponseEntity createUser(@RequestBody @Valid UserRequest req) {
		
		try {
			Optional<User> response = userService.createNewUser(req, Constants.SMS);

			return new ResponseEntity<>(response.get(), HttpStatus.OK);
		} catch (IllegalArgumentException iae) {
			return new ResponseEntity(iae.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	// api/public/user/isUserInformationUnique?name=sample&phone=7205870001&email=anAddress@domain.com
	@RequestMapping(value = { "/api/public/user/isUserInformationUnique" })
	public ResponseEntity<GenericResponseDTO> isUserInformationUnique(@RequestParam("name") String username, @RequestParam("phone") String phone, @RequestParam("email") String email) {
		GenericResponseDTO genericResponseDTO = GenericResponseDTO.builder().build();
		if (!isUserDataAvailable(UserData.USER_NAME,username)) {
			genericResponseDTO.responseMessage = "username";
		} else if (!isUserDataAvailable(UserData.USER_PHONE,phone)) {
			genericResponseDTO.responseMessage = "phone number";
		} else if (!isUserDataAvailable(UserData.USER_EMAIL,email)) {
			genericResponseDTO.responseMessage = "email";
		} else {
			genericResponseDTO.responseMessage = "true";
		}
		return ResponseEntity.status(HttpStatus.OK).body(genericResponseDTO);
	}

	private boolean isUserDataAvailable(UserData typeOfData, String data){
		boolean rtn = switch(typeOfData) {
			case USER_NAME -> ur.findByName(data).isEmpty();
			case USER_EMAIL -> ur.findByEmail(data).isEmpty();
			case USER_PHONE -> ur.findByPhone(data).get().isEmpty();
		};

		return rtn;
	}

	@RequestMapping(value = { "/api/public/user/changeLostPassword" }, method=RequestMethod.POST)
	public User changeLostPassword(@RequestBody @Valid LostPasswordRequest request) {
		return userService.changeLostPassword(request.pw, request.phoneNumber, request.smsChallengeCode);
	}
}
