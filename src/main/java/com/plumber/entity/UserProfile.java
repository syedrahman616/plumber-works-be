package com.plumber.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
	private long id;
	private String fullName;
	private String address;
	private String city;
	private String postCode;
	private String mobile;

}
