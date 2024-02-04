package com.plumber.entity;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class SocialSigninRequest {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	private String signupType;
	private String usrFullName;
	private String facebookId;
	private String usrName;
	private Map<String, Object> profile;
	private String webToken;
}
