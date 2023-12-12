package com.savvato.basemobileapp.controllers;

import javax.validation.Valid;

import com.savvato.basemobileapp.constants.Constants;
import com.savvato.basemobileapp.controllers.dto.LostPasswordRequest;
import com.savvato.basemobileapp.controllers.dto.UserRequest;
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


	// TODO: figure a way to have these enddpoints not be publicly accessible, but also not require a user to be logged in.
	// 			a secret, but general purpose for-the-app login.


	// api/public/user/isUsernameAvailable?q=sample
	@RequestMapping(value = { "/api/public/user/isUsernameAvailable" })
	public boolean isUsernameAvailable(@RequestParam("q") String queryStr) {
		return this.isUserDataAvailable(UserData.USER_NAME, queryStr);
	}

	// api/public/user/isPhoneNumberAvailable?q=7205870001
	@RequestMapping(value = { "/api/public/user/isPhoneNumberAvailable" })
	public boolean isPhoneNumberAvailable(@RequestParam("q") String queryStr) {
		Optional<List<User>> opt = this.ur.findByPhone(queryStr);

		if (opt.isPresent())
			return opt.get().size() == 0;
		else
			return true;
	}

	// api/public/user/isEmailAddressAvailable?q=anAddress@domain.com
	@RequestMapping(value = { "/api/public/user/isEmailAddressAvailable" })
	public boolean isEmailAddressAvailable(@RequestParam("q") String queryStr) {
		return this.ur.findByEmail(queryStr).isPresent() == false;
	}

	// api/public/user/isUserInformationUnique?name=sample&phone=7205870001&email=anAddress@domain.com
	@RequestMapping(value = { "/api/public/user/isUserInformationUnique" })
	public String isUserInformationUnique(@RequestParam("name") String username, @RequestParam("phone") String phone, @RequestParam("email") String email) {
		if (!isUserDataAvailable(UserData.USER_NAME,username)) return "{\"response\": \"username\"}";
		if (!isPhoneNumberAvailable(phone)) return "{\"response\": \"phone\"}";
		if (!isEmailAddressAvailable(email)) return "{\"response\": \"email\"}";

		return "{\"response\": true}";
	}

	private boolean isUserDataAvailable(UserData typeOfData, String data){
		boolean rtn = switch(typeOfData) {
			case USER_NAME -> ur.findByName(data).isPresent() == false;
			case USER_EMAIL -> ur.findByEmail(data).isPresent() == false;
			case USER_PHONE -> ur.findByPhone(data).isEmpty();
		};

		return rtn;
	}

	@RequestMapping(value = { "/api/public/user/changeLostPassword" }, method=RequestMethod.POST)
	public User changeLostPassword(@RequestBody @Valid LostPasswordRequest request) {
		return userService.changeLostPassword(request.pw, request.phoneNumber, request.smsChallengeCode);
	}
}
