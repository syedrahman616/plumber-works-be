package com.plumber.service;

import java.security.NoSuchAlgorithmException;

public interface LoginService {

	String generateToken(String receivedMail) throws NoSuchAlgorithmException;
	
}
