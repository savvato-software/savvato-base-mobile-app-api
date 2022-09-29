package com.savvato.basemobileapp.services;

public interface SMSTextMessageService {

	public boolean sendSMS(String toPhoneNumber, String msg);
	
}
