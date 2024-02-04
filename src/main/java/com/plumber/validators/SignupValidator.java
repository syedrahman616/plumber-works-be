package com.plumber.validators;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.util.StringUtils;

import com.plumber.constants.ErrorMessages;
import com.plumber.entity.SignupRequest;
import com.plumber.entity.SocialSigninRequest;
import com.plumber.validators.utils.ValidatorUtils;

public class SignupValidator {
	public static Map<String, String> validate(SignupRequest request) {
		Map<String, String> errorMapper = new HashMap<>();
		boolean errorFound = false;
		if ((StringUtils.isEmpty(request.getEmail())) || (StringUtils.isEmpty(request.getPassword()))) {
			
//			  if ((StringUtils.isEmpty(request.getFullName())) ||
//			  (StringUtils.isEmpty(request.getEmail())) ||
//			  (StringUtils.isEmpty(request.getPassword()))) {
//			 
			errorFound = true;
			errorMapper.put("Registration", ErrorMessages.REGISTRATION_FAILED);
			return errorMapper;
		}

		/*
		 * if (!ValidatorUtils.fullnameValidator(request.getFullName())) {
		 * errorMapper.put("Name", ErrorMessages.NAME_INVALID); }
		 */
		if (!ValidatorUtils.emailValidator(request.getEmail())) {
			errorMapper.put("Email", ErrorMessages.EMAIL_INVALID);
		}
		return errorMapper;
	}

	public static Map<String, String> socialvalidate(SocialSigninRequest request) {
		Map<String, String> errorMapper = new HashMap<>();
		boolean errorFound = false;
		if ((StringUtils.isEmpty(request.getEmail()))) {
			/*
			 * if ((StringUtils.isEmpty(request.getFullName())) ||
			 * (StringUtils.isEmpty(request.getEmail())) ||
			 * (StringUtils.isEmpty(request.getPassword()))) {
			 */
			errorFound = true;
			errorMapper.put("Registration", ErrorMessages.REGISTRATION_FAILED);
			return errorMapper;
		}

		/*
		 * if (!ValidatorUtils.fullnameValidator(request.getFullName())) {
		 * errorMapper.put("Name", ErrorMessages.NAME_INVALID); }
		 */
		if (!ValidatorUtils.emailValidator(request.getEmail())) {
			errorMapper.put("Email", ErrorMessages.EMAIL_INVALID);
		}
		return errorMapper;
	}

}
