package com.plumber.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.plumber.response.AuthResponse;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRequest {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	private String userRole;

}
