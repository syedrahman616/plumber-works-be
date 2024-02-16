package com.plumber.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.util.StringUtils;

import com.plumber.constants.ErrorMessages;
import com.plumber.entity.SignupRequest;
import com.plumber.validators.utils.ValidatorUtils;

public class SignupValidator {
	public static Map<String, List<String>> validate(SignupRequest request) {
		Map<String, List<String>> errorMapper = new HashMap<>();
		if (StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword())) {
			errorMapper.put("Registration", List.of(ErrorMessages.REGISTRATION_FAILED));
		}
		if (!ValidatorUtils.emailValidator(request.getEmail())) {
			errorMapper.put("email", List.of(ErrorMessages.EMAIL_INVALID));
		}
		if (!ValidatorUtils.firstNameValidator(request.getFirstName())) {
			errorMapper.put("first_Name", List.of(ErrorMessages.FIRST_NAME));
		}
		if (!ValidatorUtils.firstNameValidator(request.getLastName())) {
			errorMapper.put("last_name", List.of(ErrorMessages.LAST_NAME));
		}
		if (!ValidatorUtils.mobileValidator(request.getMobile())) {
			errorMapper.put("mobile", List.of(ErrorMessages.MOBILE_INVALID));
		}
		return errorMapper;
	}
}
