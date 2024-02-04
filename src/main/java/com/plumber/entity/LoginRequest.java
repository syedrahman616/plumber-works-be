package com.plumber.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginRequest {

	private String email;
	private String password;
	private String loginMode;
	private String webToken;
	private String signupType;
	private String facebookId;

}
