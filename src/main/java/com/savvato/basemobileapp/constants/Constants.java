package com.savvato.basemobileapp.constants;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class Constants {
	public static final String SMS = "SMS";
	public static final String EMAIL = "EMAIL";

	public static final Key JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	public static final String JWT_ISSUER = "com.savvato";

	public static final String PHOTO_SIZE_THUMBNAIL = "thumbnail";
	public static final String PHOTO_SIZE_ORIGINAL = "original";


	/////
	// Testing Related
	public static final String FAKE_USER_NAME1 = "admin";
	public static final String FAKE_USER_EMAIL1 = "admin@app.com";
	public static final String FAKE_USER_PHONE1 = "3035551212";
	public static final String FAKE_USER_NAME2 = "Bob";
	public static final String FAKE_USER_EMAIL2 = "bob@somewhereelse.com";
	public static final String FAKE_USER_PHONE2 = "3038881213";

}
