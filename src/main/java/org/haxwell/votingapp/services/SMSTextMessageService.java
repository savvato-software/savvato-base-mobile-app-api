package org.haxwell.votingapp.services;

public interface SMSTextMessageService {

	public boolean sendSMS(String toPhoneNumber, String msg);
	
}
